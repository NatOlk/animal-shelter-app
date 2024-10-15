package com.ansh.auth.service;


import com.ansh.auth.entity.User;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
    Optional<User> user = userService.findByIdentifier(identifier);
    if (user.isPresent()) {
      return new org.springframework.security.core.userdetails.User(
          user.get().getName(),
          user.get().getPassword(),
          new ArrayList<>()
      );
    } else {
      throw new UsernameNotFoundException("User not found");
    }
  }
}