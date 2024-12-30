package com.ansh.uimanagement.controller.grql;

import com.ansh.DateScalarConfiguration;
import com.ansh.auth.service.UserProfileService;
import com.ansh.entity.animal.UserProfile;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;

@GraphQlTest(controllers = GrAccountController.class)
@Import(DateScalarConfiguration.class)
class GrAccountControllerTest {

  @Autowired
  private GraphQlTester graphQlTester;

  @MockBean
  private UserProfileService userProfileService;

  @Test
  void currentUserProfile_shouldReturnUserProfile() {
    UserProfile mockProfile = new UserProfile();
    mockProfile.setId(1L);
    mockProfile.setName("John Doe");
    mockProfile.setEmail("john.doe@example.com");

    Mockito.when(userProfileService.findAuthenticatedUser())
        .thenReturn(Optional.of(mockProfile));

    String query = """
            query {
                currentUserProfile {
                    id
                    name
                    email
                }
            }
        """;

    Response response = graphQlTester.document(query)
        .execute();

    response.path("currentUserProfile.id").entity(Long.class).isEqualTo(1L);
    response.path("currentUserProfile.name").entity(String.class).isEqualTo("John Doe");
    response.path("currentUserProfile.email").entity(String.class)
        .isEqualTo("john.doe@example.com");
  }
}
