package com.ansh.cache.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.SubscriptionRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

class SubscriptionCacheManagerTest {

  private static final String CACHE_LAST_UPDATED = "cache_last_updated";
  private static final String TOPIC = "tpc";

  @InjectMocks
  private SubscriptionCacheManagerImpl subscriptionCacheManager;

  @Mock
  private SubscriptionCache subscriptionCache;

  @Mock
  private SubscriptionRepository subscriptionRepository;

  @Mock
  private RedisTemplate<String, String> updRedisTemplate;

  @Mock
  private ValueOperations<String, String> valueOperations;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(updRedisTemplate.opsForValue()).thenReturn(valueOperations);
  }

  @Test
  void testInitializeCache() {
    Subscription subscription = Subscription.builder().topic(TOPIC).build();
    List<Subscription> subscriptions = List.of(subscription);

    when(subscriptionRepository.findApprovedAndAcceptedSubscriptions())
        .thenReturn(subscriptions);

    subscriptionCacheManager.initializeCache();

    verify(subscriptionCache, times(0)).clearCache();
    verify(subscriptionCache, times(1)).addToCache(subscription);
    verify(valueOperations, times(1)).set(eq(CACHE_LAST_UPDATED), anyString());
  }

  @Test
  void testReloadCache() {
    doNothing().when(subscriptionCache).clearCache();

    subscriptionCacheManager.reloadCache();

    verify(subscriptionCache, times(1)).clearCache();
    verify(subscriptionRepository, times(1))
        .findApprovedAndAcceptedSubscriptions();
  }

  @Test
  void testShouldUpdateCacheWhenNoLastUpdated() {
    when(valueOperations.get(CACHE_LAST_UPDATED)).thenReturn(null);

    boolean result = subscriptionCacheManager.shouldUpdateCache();

    assertTrue(result);
    verify(valueOperations, times(1)).get(CACHE_LAST_UPDATED);
  }

  @Test
  void testShouldUpdateCacheWhenCacheIsStale() {
    long staleTime = System.currentTimeMillis() - (25 * 60 * 60 * 1000); // 25 hours ago
    when(valueOperations.get(CACHE_LAST_UPDATED)).thenReturn(String.valueOf(staleTime));

    boolean result = subscriptionCacheManager.shouldUpdateCache();

    assertTrue(result);
  }

  @Test
  void testShouldNotUpdateCacheWhenCacheIsFresh() {
    long freshTime = System.currentTimeMillis() - (23 * 60 * 60 * 1000); // 23 hours ago
    when(valueOperations.get(CACHE_LAST_UPDATED)).thenReturn(String.valueOf(freshTime));

    boolean result = subscriptionCacheManager.shouldUpdateCache();

    assertFalse(result);
  }

  @Test
  void testGetAllFromCacheByTopic() {
    List<Subscription> subscriptions = List.of(Subscription.builder().topic(TOPIC).build());
    when(subscriptionCache.getAllFromCache(TOPIC)).thenReturn(subscriptions);

    List<Subscription> result = subscriptionCacheManager.getAllFromCache(TOPIC);

    assertEquals(subscriptions, result);
    verify(subscriptionCache, times(1)).getAllFromCache(TOPIC);
  }

  @Test
  void testGetAllFromCacheByTopic_whenTopicIsNotExist() {
    List<Subscription> subscriptions = List.of(Subscription.builder().topic(TOPIC).build());
    when(subscriptionCache.getAllFromCache(TOPIC)).thenReturn(subscriptions);

    List<Subscription> result = subscriptionCacheManager.getAllFromCache("Fake");

    assertEquals(0, result.size());
    verify(subscriptionCache, times(1)).getAllFromCache("Fake");
  }

  @Test
  void testAddToCache() {
    Subscription subscription = new Subscription();

    subscriptionCacheManager.addToCache(subscription);

    verify(subscriptionCache, times(1)).addToCache(subscription);
  }

  @Test
  void testRemoveFromCache() {
    String token = "mock-token";

    subscriptionCacheManager.removeFromCache(token);

    verify(subscriptionCache, times(1)).removeFromCache(token);
  }
}