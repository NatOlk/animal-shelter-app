package com.ansh.auth.service;

import com.ansh.auth.repository.RoleRepository;
import com.ansh.auth.repository.UserProfileRepository;
import com.ansh.entity.animal.UserProfile;
import com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

  @Autowired
  private UserProfileRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  public Optional<UserProfile> findByIdentifier(String identifier) {
    return userRepository.findByIdentifier(identifier);
  }

  public void updateAnimalNotificationSubscriptionStatus(String identifier,
      AnimalNotificationSubscriptionStatus status) {
    userRepository.updateAnimalNotificationSubscriptionStatus(identifier, status);
  }

  public Optional<UserProfile> findAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof UserDetails userDetails) {
        String username = userDetails.getUsername();
        return findByIdentifier(username);
      }
    }
    return Optional.empty();
  }

  @Transactional
  public void updateNotificationStatusOfAuthUser(
      UserProfile.AnimalNotificationSubscriptionStatus status) {

    Optional<UserProfile> userProfile = findAuthenticatedUser();
    if (userProfile.isPresent()) {
      userProfile.get().setAnimalNotifyStatus(status);
      userRepository.save(userProfile.get());
    }
  }
}
