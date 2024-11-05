package com.ansh.auth.controller;

import com.ansh.auth.service.UserProfileService;
import com.ansh.entity.animal.UserProfile;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrAccountController {

  @Autowired
  private UserProfileService userProfileService;

  @QueryMapping
  public UserProfile currentUserProfile() {
    Optional<UserProfile> userProfileOtp = userProfileService.findAuthenticatedUser();
    return userProfileOtp.orElse(null);
  }
}
