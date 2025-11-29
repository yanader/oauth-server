package com.yanader.OAuth2.service;

import com.yanader.OAuth2.entities.AppUser;
import com.yanader.OAuth2.repository.UserRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * Called every time the user logs in.
     * Creates user if new; updates login metadata if existing.
     */
    public AppUser processLogin(OidcUser oidcUser) {

        String id = oidcUser.getSubject(); // Google unique ID

        AppUser user = repo.findById(id).orElseGet(() -> {
            AppUser u = new AppUser();
            u.setId(id);
            u.setLoginCount(0);
            u.setKeyphrase("Click to edit me!");
            return u;
        });

        // Update profile fields on every login
        user.setName(oidcUser.getFullName());
        user.setEmail(oidcUser.getEmail());
        user.setPictureUrl(oidcUser.getPicture());

        // Update login tracking
        user.setLoginCount(user.getLoginCount() + 1);
        user.setLastLogin(LocalDateTime.now());

        return repo.save(user);
    }

    public AppUser getById(String id) {
        return repo.findById(id).orElse(null);
    }

    public void updateKeyphrase(String id, String keyphrase) {
        AppUser user = repo.findById(id).orElseThrow();
        user.setKeyphrase(keyphrase);
        repo.save(user);
    }
}
