package com.ansh.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.service.user.UserProfileService;
import com.ansh.entity.account.UserProfile;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {

  @Mock
  private UserProfileService userService;

  @InjectMocks
  private CustomUserDetailsService customUserDetailsService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldLoadUserByUsernameSuccessfully() {
    String identifier = "testUser";
    UserProfile mockUser = new UserProfile();
    mockUser.setName("testUser");
    mockUser.setPassword("password123");

    when(userService.findByIdentifier(identifier)).thenReturn(Optional.of(mockUser));

    UserDetails userDetails = customUserDetailsService.loadUserByUsername(identifier);

    assertNotNull(userDetails);
    assertEquals("testUser", userDetails.getUsername());
    assertEquals("password123", userDetails.getPassword());
    assertTrue(userDetails.getAuthorities().isEmpty());

    verify(userService, times(1)).findByIdentifier(identifier);
  }

  @Test
  void shouldThrowException_whenUserNotFound() {
    String identifier = "nonexistentUser";

    when(userService.findByIdentifier(identifier)).thenReturn(Optional.empty());

    UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
        customUserDetailsService.loadUserByUsername(identifier)
    );

    assertEquals("User not found", exception.getMessage());
    verify(userService, times(1)).findByIdentifier(identifier);
  }
}
