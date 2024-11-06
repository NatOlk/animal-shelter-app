package com.ansh.auth.service;

import static com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus.ACTIVE;
import static com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus.NONE;
import static com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus.PENDING;

import com.ansh.auth.repository.RoleRepository;
import com.ansh.auth.repository.UserProfileRepository;
import com.ansh.entity.animal.UserProfile;
import com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus;
import com.ansh.entity.subscription.Subscription;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

  private static final Logger LOG = LoggerFactory.getLogger(UserProfileService.class);
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
  public void updateNotificationStatusOfAuthUser(UserProfile.AnimalNotificationSubscriptionStatus status) {

    Optional<UserProfile> userProfile = findAuthenticatedUser();
    if (userProfile.isPresent()) {
      userProfile.get().setAnimalNotifyStatus(status);
      userRepository.save(userProfile.get());
    }
  }
}
