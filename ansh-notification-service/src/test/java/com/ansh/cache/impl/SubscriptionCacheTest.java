package com.ansh.cache.impl;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

public class SubscriptionCacheTest {

  private static final String SUBSCRIPTIONS_CACHE = "animal_shelter_notification_subscriptions";
  private static final String TOKEN = "mock-token";

  private static final String TOPIC = "mock-topic";
  private static final String CACHE_KEY = STR."\{SUBSCRIPTIONS_CACHE}:\{TOPIC}";

  @InjectMocks
  private SubscriptionCache subscriptionCache;

  @Mock
  private RedisTemplate<String, Subscription> redisTemplate;

  @Mock
  private ValueOperations<String, Subscription> valueOperations;

  @Mock
  private SetOperations<String, Subscription> setOperations;

  @Mock
  private HashOperations<String, Object, Object> hashOperations;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(redisTemplate.opsForSet()).thenReturn(setOperations);
    when(redisTemplate.opsForHash()).thenReturn(hashOperations);
  }

  @Test
  void testAddToCache() {
    Subscription subscription = Subscription.builder()
        .token(TOKEN)
        .topic(TOPIC)
        .build();

    subscriptionCache.addToCache(subscription);

    verify(setOperations, times(1)).add(eq(CACHE_KEY), eq(subscription));
  }

  @Test
  void testRemoveFromCache_whenExists() {
    Subscription subscription = Subscription.builder()
        .token(TOKEN)
        .topic(TOPIC)
        .build();

    Set<Subscription> mockSubscriptions = Set.of(subscription);
    when(valueOperations.get(eq(CACHE_KEY))).thenReturn(subscription);

    Set<String> keys = Set.of(CACHE_KEY);

    when(setOperations.members(CACHE_KEY)).thenReturn(mockSubscriptions);

    when(redisTemplate.keys(STR."\{SUBSCRIPTIONS_CACHE}:*")).thenReturn(keys);

    when(hashOperations.get(CACHE_KEY, TOKEN)).thenReturn(subscription);

    subscriptionCache.removeFromCache(TOKEN);

    verify(setOperations, times(1)).remove(CACHE_KEY, subscription);
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
    Subscription subscription = Subscription.builder()
        .token(TOKEN)
        .topic(TOPIC)
        .build();

    Set<Subscription> mockSubscriptions = Set.of(subscription);
    when(valueOperations.get(eq(CACHE_KEY))).thenReturn(subscription);

    Set<String> keys = Set.of(CACHE_KEY);

    when(setOperations.members(CACHE_KEY)).thenReturn(mockSubscriptions);

    when(redisTemplate.keys(STR."\{SUBSCRIPTIONS_CACHE}:*")).thenReturn(keys);

    when(hashOperations.get(CACHE_KEY, TOKEN)).thenReturn(subscription);

    Subscription result = subscriptionCache.getFromCache(TOKEN);

    assertEquals(subscription, result);
  }

  @Test
  void testGetAllFromCache() {
    Set<String> keys = Set.of(CACHE_KEY);

    when(redisTemplate.keys(eq(STR."\{SUBSCRIPTIONS_CACHE}:*"))).thenReturn(keys);

    Subscription subscription = Subscription.builder()
        .token(TOKEN)
        .topic(TOPIC)
        .build();

    Set<Subscription> mockSubscriptions = Set.of(subscription);
    when(valueOperations.get(eq(CACHE_KEY))).thenReturn(subscription);

    when(setOperations.members(CACHE_KEY)).thenReturn(mockSubscriptions);

    List<Subscription> result = subscriptionCache.getAllFromCache(TOPIC);

    verify(setOperations, times(1)).members(eq(CACHE_KEY));
    assertEquals(1, result.size());
    assertEquals(subscription, result.getFirst());
  }
}
