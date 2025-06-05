package com.ansh.app.controller.grql;

import com.ansh.config.DateScalarConfiguration;
import com.ansh.app.service.user.UserProfileService;
import com.ansh.entity.account.UserProfile;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

@GraphQlTest(controllers = GrAccountController.class)
@Import(DateScalarConfiguration.class)
class GrAccountControllerTest {

  @Autowired
  private GraphQlTester graphQlTester;

  @MockBean
  private UserProfileService userProfileService;

  private static final Long USER_ID = 1L;
  private static final String USER_NAME = "John Doe";
  private static final String USER_EMAIL = "john.doe@example.com";

  @Test
  void allNonAdminUsers_shouldReturnList() {
    UserProfile user = new UserProfile();
    user.setId(USER_ID);
    user.setName(USER_NAME);
    user.setEmail(USER_EMAIL);

    Mockito.when(userProfileService.findAllNonAdminUsers())
        .thenReturn(List.of(user));

    String query = """
        query {
            allNonAdminUsers {
                id
                name
                email
            }
        }
        """;

    graphQlTester.document(query)
        .execute()
        .path("allNonAdminUsers[0].id").entity(Long.class).isEqualTo(USER_ID)
        .path("allNonAdminUsers[0].name").entity(String.class).isEqualTo(USER_NAME)
        .path("allNonAdminUsers[0].email").entity(String.class).isEqualTo(USER_EMAIL);
  }

  @Test
  void updateUserRoles_shouldUpdateRoles() {
    UserProfile user = new UserProfile();
    user.setId(USER_ID);
    user.setName(USER_NAME);
    user.setEmail(USER_EMAIL);

    List<String> roles = List.of("USER", "MODERATOR");

    Mockito.when(userProfileService.updateUserRoles("jane.doe", roles)).thenReturn(user);

    String mutation = """
        mutation {
            updateUserRoles(username: \"jane.doe\", roles: [\"USER\", \"MODERATOR\"]) {
                id
                name
                email
            }
        }
        """;

    graphQlTester.document(mutation)
        .execute()
        .path("updateUserRoles.id").entity(Long.class).isEqualTo(USER_ID)
        .path("updateUserRoles.name").entity(String.class).isEqualTo(USER_NAME)
        .path("updateUserRoles.email").entity(String.class).isEqualTo(USER_EMAIL);
  }
}