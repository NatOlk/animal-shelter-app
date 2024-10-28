package com.ansh.auth.service;

import com.ansh.entity.UserProfile;
import com.ansh.auth.repository.RoleRepository;
import com.ansh.auth.repository.UserProfileRepository;
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

  public UserProfile findAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof UserDetails userDetails) {
        String username = userDetails.getUsername();
        Optional<UserProfile> userProfileOpt = findByIdentifier(username);

        if (userProfileOpt.isPresent()) {
          return userProfileOpt.get();
        }
      }
    }
    return null;
  }
}