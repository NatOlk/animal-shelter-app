package com.ansh.cache.impl;

import com.ansh.cache.SubscriptionCacheManager;
import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.SubscriptionRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionCacheManagerImpl implements SubscriptionCacheManager {

  private static final Logger LOG = LoggerFactory.getLogger(SubscriptionCacheManagerImpl.class);
  private static final String CACHE_LAST_UPDATED = "cache_last_updated";
  @Autowired
  private SubscriptionCache subscriptionCache;
  @Autowired
  private SubscriptionRepository subscriptionRepository;
  @Autowired
  @Qualifier("updRedisTemplate")
  private RedisTemplate<String, String> updRedisTemplate;

  @PostConstruct
  public void init() {
    LOG.info("[Cache Init] Initializing cache at manager startup...");
    initializeCache();
  }

  @Scheduled(cron = "0 0 20 * * ?", zone = "Europe/Berlin")
  public void scheduledReloadCache() {
    try {
      if (shouldUpdateCache()) {
        LOG.info("[Cache Reload] Scheduled cache reload triggered...");
        reloadCache();
      } else {
        LOG.debug("[Cache Reload] No need to reload cache yet.");
      }
    } catch (Exception e) {
      LOG.error("[Cache Reload] Failed to reload cache: {}", e.getMessage(), e);
    }
  }

  @Override
  public void initializeCache() {
    List<Subscription> subscriptions = subscriptionRepository.findApprovedAndAcceptedSubscriptions();
    LOG.debug("[Cache Init] Initializing cache with {} subscriptions.", subscriptions.size());
    subscriptions.forEach(subscriptionCache::addToCache);
    updRedisTemplate.opsForValue()
        .set(CACHE_LAST_UPDATED, String.valueOf(System.currentTimeMillis()));
  }

  @Override
  public void reloadCache() {
    LOG.info("[Cache Reload] Reloading cache...");
    subscriptionCache.clearCache();
    initializeCache();
  }

  @Override
  public boolean shouldUpdateCache() {
    String lastUpdated = updRedisTemplate.opsForValue().get(CACHE_LAST_UPDATED);
    if (lastUpdated == null) {
      return true;
    }

    long lastUpdatedTime = Long.parseLong(lastUpdated);
    long currentTime = System.currentTimeMillis();
    return (currentTime - lastUpdatedTime) > 24 * 60 * 60 * 1000;
  }

  @Override
  public List<Subscription> getAllFromCache(String topic) {
    return subscriptionCache.getAllFromCache(topic);
  }

  @Override
  public void removeFromCache(String token) {
    subscriptionCache.removeFromCache(token);
  }

  @Override
  public void addToCache(Subscription subscription) {
    subscriptionCache.addToCache(subscription);
  }
}
