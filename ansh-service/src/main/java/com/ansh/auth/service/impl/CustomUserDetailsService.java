package com.ansh.auth.service.impl;

import com.ansh.app.service.user.impl.UserProfileServiceImpl;
import com.ansh.entity.animal.UserProfile;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
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
    Optional<UserProfile> user = userService.findByIdentifier(identifier);
    if (user.isPresent()) {
      return new User(
          user.get().getName(),
          user.get().getPassword(),
          new ArrayList<>()
      );
    } else {
      throw new UsernameNotFoundException("User not found");
    }
  }
}