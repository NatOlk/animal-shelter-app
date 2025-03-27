package com.ansh.app.controller.grql;

import com.ansh.app.service.user.impl.UserProfileServiceImpl;
import com.ansh.entity.account.UserProfile;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrAccountController {

  @Autowired
  private UserProfileServiceImpl userProfileService;

  @QueryMapping
  public UserProfile currentUserProfile() {
    Optional<UserProfile> userProfileOtp = userProfileService.getAuthUser();
    return userProfileOtp.orElse(null);
  }
}
