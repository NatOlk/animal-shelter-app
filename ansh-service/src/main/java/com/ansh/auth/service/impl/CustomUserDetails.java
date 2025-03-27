package com.ansh.auth.service.impl;

import com.ansh.entity.account.Role;
import com.ansh.entity.account.UserProfile;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private final UserProfile user;

  public CustomUserDetails(UserProfile user) {
    this.user = user;
  }

  public String getEmail() {
    return user.getEmail();
  }

  public String getName() {
    return user.getName();
  }

  public Long getId() {
    return user.getId();
  }

  public List<String> getRoleNames() {
    return user.getRoles().stream()
        .map(Role::getName)
        .toList();
  }

  public UserProfile getUserProfile() {
    return user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(STR."ROLE_\{role.getName()}"))
        .toList();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getName();
  }

  @Override
  public boolean isAccountNonExpired() { return true; }

  @Override
  public boolean isAccountNonLocked() { return true; }

  @Override
  public boolean isCredentialsNonExpired() { return true; }

  @Override
  public boolean isEnabled() { return true; }
}
