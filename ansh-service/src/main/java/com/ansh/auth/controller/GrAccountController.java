package com.ansh.auth.controller;

import com.ansh.auth.service.UserService;
import com.ansh.entity.UserProfile;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrAccountController {

  @Autowired
  private UserService userService;

  @QueryMapping
  public UserProfile currentUserProfile() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof UserDetails userDetails) {
        String username = userDetails.getUsername();
        Optional<UserProfile> userProfileOpt = userService.findByIdentifier(username);

        if (userProfileOpt.isPresent()) {
          return userProfileOpt.get();
        }
      }
    }
    return null;
  }
}
