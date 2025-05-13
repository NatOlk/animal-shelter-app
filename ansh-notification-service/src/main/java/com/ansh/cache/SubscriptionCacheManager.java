package com.ansh.cache;

import com.ansh.entity.subscription.Subscription;
import java.util.List;

/**
 * Interface for managing the subscription cache.
 * Defines operations for initializing, updating, retrieving and modifying cache entries.
 */
public interface SubscriptionCacheManager {

  /**
   * Initializes the cache with the current subscriptions from the database.
   * Should be called at application startup or after a full reset.
   */
  void initializeCache();

  /**
   * Reloads the cache completely by clearing existing entries and fetching fresh data.
   */
  void reloadCache();

  /**
   * Determines whether the cache should be updated.
   *
   * @return true if the cache needs to be updated, false otherwise
   */
  boolean shouldUpdateCache();

  /**
   * Retrieves all subscriptions for a given topic from the cache.
   *
   * @param topic the topic for which subscriptions are requested
   * @return list of subscriptions matching the topic
   */
  List<Subscription> getAllFromCache(String topic);

  /**
   * Removes a subscription from the cache by its unique token.
   *
   * @param token the unique subscription token
   */
  void removeFromCache(String token);

  /**
   * Adds a subscription to the cache.
   *
   * @param subscription the subscription to be added
   */
  void addToCache(Subscription subscription);
}