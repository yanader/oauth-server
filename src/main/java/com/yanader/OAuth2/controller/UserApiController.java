package com.yanader.OAuth2.controller;

import com.yanader.OAuth2.entities.AppUser;
import com.yanader.OAuth2.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public AppUser me(@AuthenticationPrincipal OidcUser oidcUser) {
        return userService.processLogin(oidcUser);
    }
}
