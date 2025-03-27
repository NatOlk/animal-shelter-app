package com.ansh.app.service.user;

import com.ansh.app.service.exception.user.UserAlreadyExistException;
import com.ansh.entity.account.UserProfile;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.lang.NonNull;

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
  Optional<UserProfile> findByIdentifier(@NonNull String identifier);

  /**
   * Registers a new user with the provided email and password.
   *
   * @param identifier the identifier of the user to be registered (must be unique)
   * @param email the email address of the user to be registered (must be unique)
   * @param password the password for the new user (will be encoded before saving)
   * @return the created UserProfile entity
   * @throws UserAlreadyExistException if the email is already in use or invalid
   */
  UserProfile registerUser(@NonNull String identifier, @NonNull String email, @NonNull String password) throws UserAlreadyExistException;

  /**
   * Updates the animal notification subscription status for a user.
   *
   * @param identifier the unique user identifier
   * @param status     the new subscription status
   */
  void updateAnimalNotificationSubscriptionStatus(@NonNull String identifier, @NonNull AnimalInfoNotifStatus status);

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
  void updateNotificationStatusOfAuthUser(@NonNull AnimalInfoNotifStatus status);

  /**
   * Updates the roles of a user identified by username.
   * Roles are provided as strings and stored in the user profile as a comma-separated string.
   *
   * @param username   the unique identifier of the user (e.g. login name)
   * @param roleNames  a list of role names to assign to the user (e.g. ["ADMIN", "EMPLOYEE"])
   * @return the updated {@link UserProfile} entity with new roles
   */
  UserProfile updateUserRoles(String username, List<String> roleNames);
}
