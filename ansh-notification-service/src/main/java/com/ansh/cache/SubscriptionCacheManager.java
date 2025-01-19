package com.ansh.cache;

import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.SubscriptionRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
@Component
public class SubscriptionCacheManager {

  private static final Logger LOG = LoggerFactory.getLogger(SubscriptionCacheManager.class);
  private static final String CACHE_LAST_UPDATED = "cache_last_updated";

  private final SubscriptionCache subscriptionCache;
  private final SubscriptionRepository subscriptionRepository;
  private final RedisTemplate<String, String> updRedisTemplate;
  private final String animalTopicId;

  @Autowired
  public SubscriptionCacheManager(
      SubscriptionCache subscriptionCache,
      SubscriptionRepository subscriptionRepository,
      @Qualifier("updRedisTemplate") RedisTemplate<String, String> updRedisTemplate,
      @Value("${animalTopicId}") String animalTopicId) {
    this.subscriptionCache = subscriptionCache;
    this.subscriptionRepository = subscriptionRepository;
    this.updRedisTemplate = updRedisTemplate;
    this.animalTopicId = animalTopicId;
  }

  public void initializeCache() {
    List<Subscription> subscriptions = subscriptionRepository.findByTopicAndAcceptedTrueAndApprovedTrue(animalTopicId);
    LOG.debug("[Cache Init] Initializing cache with {} subscriptions.", subscriptions.size());
    subscriptionCache.clearCache();
    subscriptions.forEach(subscriptionCache::addToCache);
    updRedisTemplate.opsForValue().set(CACHE_LAST_UPDATED, String.valueOf(System.currentTimeMillis()));
  }

  public void reloadCache() {
    LOG.info("[Cache Reload] Reloading cache...");
    subscriptionCache.clearCache();
    initializeCache();
  }

  public boolean shouldUpdateCache() {
    String lastUpdated = updRedisTemplate.opsForValue().get(CACHE_LAST_UPDATED);
    if (lastUpdated == null) return true;

    long lastUpdatedTime = Long.parseLong(lastUpdated);
    long currentTime = System.currentTimeMillis();
    return (currentTime - lastUpdatedTime) > 24 * 60 * 60 * 1000;
  }

  public List<Subscription> getAllFromCache() {
    return subscriptionCache.getAllFromCache();
  }

  public void removeFromCache(String token) {
    subscriptionCache.removeFromCache(token);
  }

  public void addToCache(Subscription subscription) {
    subscriptionCache.addToCache(subscription);
  }
}
