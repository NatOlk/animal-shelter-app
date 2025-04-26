package com.ansh.cache.impl;

import com.ansh.entity.subscription.Subscription;
import com.ansh.utils.IdentifierMasker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
class SubscriptionCache {

  private static final Logger LOG = LoggerFactory.getLogger(SubscriptionCache.class);
  private static final String CACHE_PREFIX = "animal_shelter_notification_subscriptions";

  private final RedisTemplate<String, Subscription> redisTemplate;

  public SubscriptionCache(
      @Qualifier("subscriptionRedisTemplate") RedisTemplate<String, Subscription> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void addToCache(Subscription subscription) {
    var key = getTopicKey(subscription.getTopic());
    LOG.debug("[cache] Adding subscription for topic={} and token={}", subscription.getTopic(),
        IdentifierMasker.maskIdentifier(subscription.getToken()));
    redisTemplate.opsForSet().add(key, subscription);
  }

  public void removeFromCache(String token) {
    LOG.debug("[cache] Removing subscription by token={}", IdentifierMasker.maskIdentifier(token));
    var keys = redisTemplate.keys(STR."\{CACHE_PREFIX}:*");
    if (keys != null) {
      for (String key : keys) {
        var subs = redisTemplate.opsForSet().members(key);
        if (subs != null) {
          subs.stream()
              .filter(sub -> token.equals(sub.getToken()))
              .findFirst()
              .ifPresent(sub -> {
                redisTemplate.opsForSet().remove(key, sub);
                LOG.debug("[cache] Removed subscription from key={}", key);
              });
        }
      }
    }
  }

  public Subscription getFromCache(String token) {
    Set<String> keys = redisTemplate.keys(STR."\{CACHE_PREFIX}:*");
    if (keys == null) return null;

    for (String key : keys) {
      Subscription sub = (Subscription) redisTemplate.opsForHash().get(key, token);
      if (sub != null) {
        return sub;
      }
    }

    return null;
  }

  public List<Subscription> getAllFromCache(String topic) {
    var key = getTopicKey(topic);
    var set = redisTemplate.opsForSet().members(key);
    return set == null ? Collections.emptyList() : new ArrayList<>(set);
  }

  public void clearCache() {
    var keys = redisTemplate.keys(STR."\{CACHE_PREFIX}:*");
    if (keys != null) {
      keys.forEach(redisTemplate::delete);
      LOG.debug("[cache] Cleared all subscription topics");
    }
  }

  private String getTopicKey(String topic) {
    return STR."\{CACHE_PREFIX}:\{topic}";
  }
}
