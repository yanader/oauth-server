package com.yanader.OAuth2.controller;

import com.yanader.OAuth2.entities.AppUser;
import com.yanader.OAuth2.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OidcUser oidcUser, Model model) {

        // Ensure user exists & update login tracking
        AppUser appUser = userService.processLogin(oidcUser);

        model.addAttribute("appUser", appUser);
        return "profile";
    }

    @PostMapping("/profile/keyphrase")
    public String updateKeyphrase(
            @AuthenticationPrincipal OidcUser oidcUser,
            @RequestParam String keyphrase
    ) {
        userService.updateKeyphrase(oidcUser.getSubject(), keyphrase);
        return "redirect:/profile";
    }
}
