package com.ansh.uimanagement.controller;

import com.ansh.AnshSecurityConfig;
import com.ansh.auth.service.CustomUserDetailsService;
import com.ansh.auth.service.JwtService;
import com.ansh.auth.service.UserProfileService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@Import(AnshSecurityConfig.class)
public abstract class AbstractControllerWebMvcTest {

  protected static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;
  protected static final String BEARER_TOKEN = "Bearer mockToken";
  protected static final String MOCK_TOKEN = "mockToken";
  protected static final String USERNAME = "test@example.com";

  @Autowired
  protected MockMvc mockMvc;

  @MockBean
  protected UserProfileService userProfileService;

  @MockBean
  protected JwtService jwtService;

  @MockBean
  protected CustomUserDetailsService customUserDetailsService;

  @BeforeEach
  void setUpBase() {
    UserDetails mockUserDetails = new User(
        USERNAME,
        "password",
        List.of(new SimpleGrantedAuthority("ADMIN"))
    );

    Mockito.when(customUserDetailsService.loadUserByUsername(USERNAME))
        .thenReturn(mockUserDetails);
    Mockito.when(jwtService.isTokenValid(Mockito.eq(MOCK_TOKEN), Mockito.eq(mockUserDetails)))
        .thenReturn(true);
    Mockito.when(jwtService.extractUsername(MOCK_TOKEN))
        .thenReturn(USERNAME);
  }
}
