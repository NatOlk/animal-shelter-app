package com.ansh.cache;

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

  private static final String ANIMAL_TOPIC_ID = "test-topic";
  private static final String CACHE_LAST_UPDATED = "cache_last_updated";

  @InjectMocks
  private SubscriptionCacheManager subscriptionCacheManager;

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
    subscriptionCacheManager.setAnimalTopicId(ANIMAL_TOPIC_ID);
  }

  @Test
  void testInitializeCache() {
    Subscription subscription = new Subscription();
    List<Subscription> subscriptions = List.of(subscription);

    when(subscriptionRepository.findApprovedAndAcceptedSubscriptionsByTopic(ANIMAL_TOPIC_ID))
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
        .findApprovedAndAcceptedSubscriptionsByTopic(eq(ANIMAL_TOPIC_ID));
  }

  @Test
  void testShouldUpdateCacheWhenNoLastUpdated() {
    when(valueOperations.get(eq(CACHE_LAST_UPDATED))).thenReturn(null);

    boolean result = subscriptionCacheManager.shouldUpdateCache();

    assertTrue(result);
    verify(valueOperations, times(1)).get(eq(CACHE_LAST_UPDATED));
  }

  @Test
  void testShouldUpdateCacheWhenCacheIsStale() {
    long staleTime = System.currentTimeMillis() - (25 * 60 * 60 * 1000); // 25 hours ago
    when(valueOperations.get(eq(CACHE_LAST_UPDATED))).thenReturn(String.valueOf(staleTime));

    boolean result = subscriptionCacheManager.shouldUpdateCache();

    assertTrue(result);
  }

  @Test
  void testShouldNotUpdateCacheWhenCacheIsFresh() {
    long freshTime = System.currentTimeMillis() - (23 * 60 * 60 * 1000); // 23 hours ago
    when(valueOperations.get(eq(CACHE_LAST_UPDATED))).thenReturn(String.valueOf(freshTime));

    boolean result = subscriptionCacheManager.shouldUpdateCache();

    assertFalse(result);
  }

  @Test
  void testGetAllFromCache() {
    List<Subscription> subscriptions = List.of(new Subscription());
    when(subscriptionCache.getAllFromCache()).thenReturn(subscriptions);

    List<Subscription> result = subscriptionCacheManager.getAllFromCache();

    assertEquals(subscriptions, result);
    verify(subscriptionCache, times(1)).getAllFromCache();
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