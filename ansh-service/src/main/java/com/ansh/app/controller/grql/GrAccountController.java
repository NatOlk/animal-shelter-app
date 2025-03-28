package com.ansh.app.controller.grql;

import com.ansh.app.service.user.UserProfileService;
import com.ansh.entity.account.UserProfile;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrAccountController {

  @Autowired
  private UserProfileService userProfileService;

  //TODO: not needed ?
  @QueryMapping
  public UserProfile currentUserProfile() {
    Optional<UserProfile> userProfileOtp = userProfileService.getAuthUser();
    return userProfileOtp.orElse(null);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @MutationMapping
  public UserProfile updateUserRoles(@Argument String username, @Argument List<String> roles) {
    return userProfileService.updateUserRoles(username, roles);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @QueryMapping
  public List<UserProfile> allNonAdminUsers() {
    return userProfileService.findAllNonAdminUsers();
  }

  @GraphQlExceptionHandler
  public GraphQLError handle(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment) {
    return GraphQLError.newError()
        .errorType(ErrorType.BAD_REQUEST)
        .message(ex.getMessage())
        .path(environment.getExecutionStepInfo().getPath())
        .location(environment.getField().getSourceLocation()).build();
  }
}
