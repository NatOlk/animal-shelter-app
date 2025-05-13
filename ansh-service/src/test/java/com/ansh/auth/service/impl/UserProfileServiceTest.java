package com.ansh.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.service.user.impl.UserProfileServiceImpl;
import com.ansh.auth.repository.UserProfileRepository;
import com.ansh.entity.account.UserProfile;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.account.UserProfile.Role;
import com.ansh.dto.NotificationStatusDTO;
import java.util.List;
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
  void shouldUpdateNotificationStatusesOfAuthUser() {
    // given
    NotificationStatusDTO statusDTO = new NotificationStatusDTO(
        AnimalInfoNotifStatus.ACTIVE,
        AnimalInfoNotifStatus.PENDING,
        AnimalInfoNotifStatus.UNKNOWN
    );

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(customUserDetails);

    SecurityContextHolder.setContext(securityContext);

    // when
    userProfileService.updateNotificationStatusOfAuthUser(statusDTO);

    // then
    assertEquals(AnimalInfoNotifStatus.ACTIVE, mockUserProfile.getAnimalNewsNotifyStatus());
    assertEquals(AnimalInfoNotifStatus.PENDING, mockUserProfile.getAnimalNotifyStatus());
    assertEquals(AnimalInfoNotifStatus.UNKNOWN, mockUserProfile.getVaccinationNotifyStatus());
    verify(userRepository, times(1)).save(mockUserProfile);
  }

  @Test
  void shouldUpdateUserRolesSuccessfully() {
    String username = "john";
    List<String> roles = List.of("ADMIN", "DOCTOR");

    UserProfile user = new UserProfile();
    user.setName(username);

    when(userRepository.findByIdentifier(username)).thenReturn(Optional.of(user));
    when(userRepository.save(any(UserProfile.class))).thenAnswer(
        invocation -> invocation.getArgument(0));

    UserProfile updatedUser = userProfileService.updateUserRoles(username, roles);

    assertNotNull(updatedUser);
    assertEquals(Set.of(Role.ADMIN, Role.DOCTOR), updatedUser.getRoles());
  }

  @Test
  void shouldReturnNull_whenUserNotFound() {
    String username = "notfound";
    List<String> roles = List.of("USER");

    when(userRepository.findByIdentifier(username)).thenReturn(Optional.empty());

    UserProfile result = userProfileService.updateUserRoles(username, roles);

    assertNull(result);
    verify(userRepository, never()).save(any());
  }
}