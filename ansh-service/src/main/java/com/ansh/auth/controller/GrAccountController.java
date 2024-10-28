package com.ansh.auth.controller;

import com.ansh.auth.service.UserProfileService;
import com.ansh.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrAccountController {

  @Autowired
  private UserProfileService userProfileService;

  @QueryMapping
  public UserProfile currentUserProfile() {
    return userProfileService.findAuthenticatedUser();
  }
}
