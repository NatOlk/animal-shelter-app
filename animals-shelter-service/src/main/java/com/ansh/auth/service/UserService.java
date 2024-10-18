package com.ansh.auth.service;

import com.ansh.auth.entity.UserProfile;
import com.ansh.auth.repository.RoleRepository;
import com.ansh.auth.repository.UserProfileRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
  @Autowired
  private UserProfileRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  public Optional<UserProfile> findByIdentifier(String identifier) {
    return userRepository.findByIdentifier(identifier);
  }
}