package com.ansh.app.service.user;

import com.ansh.entity.animal.UserProfile;
import com.ansh.entity.animal.UserProfile.AnimalNotifStatus;
import java.util.Optional;

/**
 * Service for managing user profiles.
 */
public interface UserProfileService {

  /**
   * Finds a user profile by its unique identifier.
   *
   * @param identifier the unique user identifier
   * @return {@link Optional} containing the {@link UserProfile} if found, otherwise an empty
   * Optional
   */
  Optional<UserProfile> findByIdentifier(String identifier);

  /**
   * Updates the animal notification subscription status for a user.
   *
   * @param identifier the unique user identifier
   * @param status     the new subscription status
   */
  void updateAnimalNotificationSubscriptionStatus(String identifier, AnimalNotifStatus status);

  /**
   * Retrieves the profile of the currently authenticated user.
   *
   * @return {@link Optional} containing the {@link UserProfile} if the user is authenticated,
   * otherwise an empty Optional
   */
  Optional<UserProfile> getAuthUser();

  /**
   * Updates the notification subscription status for the currently authenticated user.
   *
   * @param status the new subscription status
   */
  void updateNotificationStatusOfAuthUser(AnimalNotifStatus status);
}
