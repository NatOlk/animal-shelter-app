package com.ansh.auth.service.impl;

import com.ansh.app.service.user.impl.UserProfileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserProfileServiceImpl userService;

  @Override
  public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
    return userService.findByIdentifier(identifier)
        .map(CustomUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}