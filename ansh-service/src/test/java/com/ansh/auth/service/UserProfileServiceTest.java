package com.ansh.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.ansh.auth.repository.UserProfileRepository;
import com.ansh.entity.animal.UserProfile;
import com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

class UserProfileServiceTest {

  @Mock
  private UserProfileRepository userRepository;

  @Mock
  private Authentication authentication;

  @Mock
  private SecurityContext securityContext;

  @Mock
  private UserDetails userDetails;

  @InjectMocks
  private UserProfileService userProfileService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldFindByIdentifier() {
    String identifier = "test_user";
    UserProfile userProfile = new UserProfile();
    when(userRepository.findByIdentifier(identifier)).thenReturn(Optional.of(userProfile));

    Optional<UserProfile> result = userProfileService.findByIdentifier(identifier);

    assertTrue(result.isPresent());
    assertEquals(userProfile, result.get());
  }

  @Test
  void shouldUpdateAnimalNotificationSubscriptionStatus() {
    String identifier = "test_user";
    AnimalNotificationSubscriptionStatus status = AnimalNotificationSubscriptionStatus.PENDING;

    userProfileService.updateAnimalNotificationSubscriptionStatus(identifier, status);

    verify(userRepository, times(1)).updateAnimalNotificationSubscriptionStatus(identifier, status);
  }

  @Test
  void shouldFindAuthenticatedUser() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(userDetails.getUsername()).thenReturn("test_user");
    when(userRepository.findByIdentifier("test_user")).thenReturn(Optional.of(new UserProfile()));

    SecurityContextHolder.setContext(securityContext);
    Optional<UserProfile> result = userProfileService.findAuthenticatedUser();

    assertTrue(result.isPresent());
  }

  @Test
  void shouldUpdateNotificationStatusOfAuthUser() {
    AnimalNotificationSubscriptionStatus status = AnimalNotificationSubscriptionStatus.ACTIVE;
    UserProfile userProfile = new UserProfile();
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(userDetails.getUsername()).thenReturn("test_user");
    when(userRepository.findByIdentifier("test_user")).thenReturn(Optional.of(userProfile));

    SecurityContextHolder.setContext(securityContext);
    userProfileService.updateNotificationStatusOfAuthUser(status);

    assertEquals(status, userProfile.getAnimalNotifyStatus());
    verify(userRepository, times(1)).save(userProfile);
  }
}
