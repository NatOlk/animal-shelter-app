package com.ansh.auth.controller;

import com.ansh.auth.entity.UserProfile;
import com.ansh.auth.service.UserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrAccountController {

    private static final Logger LOG = LoggerFactory.getLogger(GrAccountController.class);

    @Autowired
    private UserService userService;

    @QueryMapping
    public UserProfile currentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();
                Optional<UserProfile> userProfileOpt = userService.findByIdentifier(username);

                if(userProfileOpt.isPresent()) {
                    LOG.info(userProfileOpt.get().toString());
                    return userProfileOpt.get();
                }
            }
         }
        return null;
    }
}
