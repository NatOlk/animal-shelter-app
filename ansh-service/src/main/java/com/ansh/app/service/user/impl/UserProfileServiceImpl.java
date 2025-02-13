package com.ansh.app.service.user.impl;

import com.ansh.app.service.user.UserProfileService;
import com.ansh.auth.repository.RoleRepository;
import com.ansh.auth.repository.UserProfileRepository;
import com.ansh.entity.animal.UserProfile;
import com.ansh.entity.animal.UserProfile.AnimalNotifStatus;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

  @Autowired
  private UserProfileRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public Optional<UserProfile> findByIdentifier(String identifier) {
    return userRepository.findByIdentifier(identifier);
  }

  @Override
  public void updateAnimalNotificationSubscriptionStatus(String identifier,
      AnimalNotifStatus status) {
    userRepository.updateAnimalNotificationSubscriptionStatus(identifier, status);
  }

  @Override
  public Optional<UserProfile> getAuthUser() {
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

  @Override
  @Transactional
  public void updateNotificationStatusOfAuthUser(AnimalNotifStatus status) {
    Optional<UserProfile> userProfile = getAuthUser();
    if (userProfile.isPresent()) {
      userProfile.get().setAnimalNotifyStatus(status);
      userRepository.save(userProfile.get());
    }
  }
}
