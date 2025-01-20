package com.ansh.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.entity.subscription.Subscription;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class SubscriptionCacheTest {
  private static final String SUBSCRIPTIONS_CACHE = "animal_notification_subscriptions";
  private static final String TOKEN = "mock-token";
  private static final String CACHE_KEY = STR."\{SUBSCRIPTIONS_CACHE}:\{TOKEN}";

  private SubscriptionCache subscriptionCache;

  @Mock
  private RedisTemplate<String, Subscription> redisTemplate;

  @Mock
  private ValueOperations<String, Subscription> valueOperations;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    subscriptionCache = new SubscriptionCache(redisTemplate);
  }

  @Test
  void testAddToCache() {
    Subscription subscription = new Subscription();
    subscription.setToken(TOKEN);

    subscriptionCache.addToCache(subscription);

    verify(valueOperations, times(1)).set(eq(CACHE_KEY), eq(subscription));
  }

  @Test
  void testRemoveFromCache() {
    subscriptionCache.removeFromCache(TOKEN);

    verify(redisTemplate, times(1)).delete(eq(CACHE_KEY));
  }

  @Test
  void testClearCache() {
    Set<String> keys = Set.of(CACHE_KEY);
    when(redisTemplate.keys(any())).thenReturn(keys);

    subscriptionCache.clearCache();

    verify(redisTemplate, times(1)).keys(eq(STR."\{SUBSCRIPTIONS_CACHE}:*"));
    verify(redisTemplate, times(keys.size())).delete(any(String.class));
  }

  @Test
  void testGetFromCache() {
    Subscription expectedSubscription = new Subscription();
    when(valueOperations.get(eq(CACHE_KEY))).thenReturn(expectedSubscription);

    Subscription result = subscriptionCache.getFromCache(TOKEN);

    verify(valueOperations, times(1)).get(eq(CACHE_KEY));
    assertEquals(expectedSubscription, result);
  }

  @Test
  void testGetAllFromCache() {
    Set<String> keys = Set.of(CACHE_KEY);
    Subscription subscription = new Subscription();

    when(redisTemplate.keys(eq(STR."\{SUBSCRIPTIONS_CACHE}:*"))).thenReturn(keys);
    when(valueOperations.get(eq(CACHE_KEY))).thenReturn(subscription);

    List<Subscription> result = subscriptionCache.getAllFromCache();

    verify(redisTemplate, times(1)).keys(eq(STR."\{SUBSCRIPTIONS_CACHE}:*"));
    verify(valueOperations, times(1)).get(eq(CACHE_KEY));
    assertEquals(1, result.size());
    assertEquals(subscription, result.getFirst());
  }
}
