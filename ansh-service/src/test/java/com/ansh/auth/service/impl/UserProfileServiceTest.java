package com.ansh.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.ansh.entity.account.UserProfile;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.app.service.user.impl.UserProfileServiceImpl;
import com.ansh.auth.repository.UserProfileRepository;
import com.ansh.entity.account.UserProfile.Role;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class UserProfileServiceTest {

  @Mock
  private UserProfileRepository userRepository;

  @Mock
  private Authentication authentication;

  @Mock
  private SecurityContext securityContext;

  @InjectMocks
  private UserProfileServiceImpl userProfileService;

  private UserProfile mockUserProfile;

  private CustomUserDetails customUserDetails;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockUserProfile = new UserProfile();
    mockUserProfile.setName("test_user");
    mockUserProfile.setEmail("test@example.com");
    mockUserProfile.setRoles(Set.of(Role.ADMIN));

    customUserDetails = new CustomUserDetails(mockUserProfile);
  }

  @Test
  void shouldFindByIdentifier() {
    String identifier = "test_user";
    when(userRepository.findByIdentifier(identifier)).thenReturn(Optional.of(mockUserProfile));

    Optional<UserProfile> result = userProfileService.findByIdentifier(identifier);

    assertTrue(result.isPresent());
    assertEquals(mockUserProfile, result.get());
  }

  @Test
  void shouldUpdateAnimalNotificationSubscriptionStatus() {
    String identifier = "test_user";
    AnimalInfoNotifStatus status = AnimalInfoNotifStatus.PENDING;

    userProfileService.updateAnimalNotificationSubscriptionStatus(identifier, status);

    verify(userRepository, times(1)).updateAnimalNotificationSubscriptionStatus(identifier, status);
  }

  @Test
  void shouldFindAuthenticatedUser() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(customUserDetails);

    SecurityContextHolder.setContext(securityContext);
    Optional<UserProfile> result = userProfileService.getAuthUser();

    assertTrue(result.isPresent());
    assertEquals(mockUserProfile, result.get());
  }

  @Test
  void shouldUpdateNotificationStatusOfAuthUser() {
    AnimalInfoNotifStatus status = AnimalInfoNotifStatus.ACTIVE;

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(customUserDetails);

    SecurityContextHolder.setContext(securityContext);
    userProfileService.updateNotificationStatusOfAuthUser(status);

    assertEquals(status, mockUserProfile.getAnimalNotifyStatus());
    verify(userRepository, times(1)).save(mockUserProfile);
  }
}
