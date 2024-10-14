package com.ansh.auth.controller;

import com.ansh.auth.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
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
    private HttpServletRequest request;

    @Transactional
    @QueryMapping
    public User currentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String username = userDetails.getUsername();
                String password = userDetails.getPassword();

                LOG.info("Username: " + username);
            }
         }
        User user = (User) request.getSession().getAttribute("user");
        return user;
    }
}
