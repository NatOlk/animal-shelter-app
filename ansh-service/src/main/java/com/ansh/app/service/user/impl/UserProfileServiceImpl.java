package com.ansh.app.service.user.impl;

import com.ansh.app.service.exception.user.UserAlreadyExistException;
import com.ansh.app.service.user.UserProfileService;
import com.ansh.auth.repository.UserProfileRepository;
import com.ansh.auth.service.impl.CustomUserDetails;
import com.ansh.entity.account.UserProfile;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.account.UserProfile.Role;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

  @Autowired
  private UserProfileRepository userRepository;

  @Override
  public Optional<UserProfile> findByIdentifier(@NonNull String identifier) {
    return userRepository.findByIdentifier(identifier);
  }

  @Override
  @Transactional
  public UserProfile registerUser(@NonNull String identifier, @NonNull String email,
      @NonNull String password) throws UserAlreadyExistException {
    if (userRepository.findByEmailOrIdentifier(email, identifier).isPresent()) {
      throw new UserAlreadyExistException("Identifier OR Email already in use");
    }

    String encodedPassword = new BCryptPasswordEncoder().encode(password);

    UserProfile userProfile = new UserProfile();
    userProfile.setName(identifier);
    userProfile.setEmail(email);
    userProfile.setPassword(encodedPassword);
    userProfile.setRoles(Set.of(Role.USER));

    userRepository.save(userProfile);

    return userProfile;
  }

  @Override
  @Transactional
  public void updateAnimalNotificationSubscriptionStatus(@NonNull String identifier,
      @NonNull AnimalInfoNotifStatus status) {
    userRepository.updateAnimalNotificationSubscriptionStatus(identifier, status);
  }

  @Override
  public Optional<UserProfile> getAuthUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof CustomUserDetails customUserDetails) {
        return Optional.of(customUserDetails.getUserProfile());
      }
    }
    return Optional.empty();
  }

  @Override
  @Transactional
  public void updateNotificationStatusOfAuthUser(@NonNull AnimalInfoNotifStatus status) {
    Optional<UserProfile> userProfile = getAuthUser();
    if (userProfile.isPresent()) {
      userProfile.get().setAnimalNotifyStatus(status);
      userRepository.save(userProfile.get());
    }
  }

  @Override
  @Transactional
  public UserProfile updateUserRoles(String username, List<String> roleNames) {
    return userRepository.findByIdentifier(username)
        .map(user -> {
          Set<UserProfile.Role> roleSet = roleNames.stream()
              .map(String::trim)
              .map(String::toUpperCase)
              .map(UserProfile.Role::valueOf)
              .collect(Collectors.toSet());

          user.setRoles(roleSet);
          return userRepository.save(user);
        })
        .orElse(null);
  }

  @Override
  public List<UserProfile> findAllNonAdminUsers() {
    return userRepository.findAllNonAdminUsers(Role.ADMIN.name());
  }
}
