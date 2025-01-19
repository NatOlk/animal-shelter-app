package com.ansh.cache;

import com.ansh.entity.subscription.Subscription;
import com.ansh.utils.IdentifierMasker;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionCache {
  private static final Logger LOG = LoggerFactory.getLogger(SubscriptionCache.class);

  private static final String SUBSCRIPTIONS_CACHE = "animal_notification_subscriptions";

  private final RedisTemplate<String, Subscription> subscriptionRedisTemplate;

  public SubscriptionCache(
      @Qualifier("subscriptionRedisTemplate") RedisTemplate<String, Subscription> subscriptionRedisTemplate) {
    this.subscriptionRedisTemplate = subscriptionRedisTemplate;
  }

  public void addToCache(Subscription subscription) {
    String key = getCacheKey(subscription.getToken());
    LOG.debug("[subscribers cache] [add] Add key for token {}", IdentifierMasker.maskIdentifier(subscription.getToken()));
    subscriptionRedisTemplate.opsForValue().set(key, subscription);
  }


  public void removeFromCache(String token) {
    String key = getCacheKey(token);
    subscriptionRedisTemplate.delete(key);
    LOG.debug("[subscribers cache] [remove] Subscription removed from cache and DB with key {}",
        IdentifierMasker.maskIdentifier(token));
  }

  public void clearCache() {
    Optional.ofNullable(subscriptionRedisTemplate.keys(STR."\{SUBSCRIPTIONS_CACHE}:*"))
        .ifPresent(keys -> keys.forEach(subscriptionRedisTemplate::delete));
  }

  public Subscription getFromCache(String token) {
    String key = getCacheKey(token);
    return subscriptionRedisTemplate.opsForValue().get(key);
  }


  public List<Subscription> getAllFromCache() {
    Set<String> keys = subscriptionRedisTemplate.keys(STR."\{SUBSCRIPTIONS_CACHE}:*");
    if (keys == null || keys.isEmpty()) {
      return List.of();
    }
    return keys.stream()
        .map(key -> subscriptionRedisTemplate.opsForValue().get(key))
        .toList();
  }

  private String getCacheKey(String token) {
    return STR."\{SUBSCRIPTIONS_CACHE}:\{token}";
  }
}
