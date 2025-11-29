package com.yanader.OAuth2.config;

import com.yanader.OAuth2.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Custom resolver to add prompt=select_account for Google login
    @Bean
    public OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository
    ) {
        DefaultOAuth2AuthorizationRequestResolver resolver =
                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

        resolver.setAuthorizationRequestCustomizer(customizer ->
                customizer.additionalParameters(params ->
                        params.put("prompt", "select_account")
                )
        );

        return resolver;
    }

    // Security filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           OAuth2AuthorizationRequestResolver resolver,
                                           UserService userService) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestResolver(resolver)
                        )
                        .userInfoEndpoint(userInfo ->
                                userInfo.oidcUserService(oidcUserService(userService))
                        )
                        .loginPage("/login")
                        .defaultSuccessUrl("/profile", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    // Custom OidcUserService that updates or creates the user in DB
    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService(UserService userService) {
        OidcUserService delegate = new OidcUserService();

        return request -> {
            OidcUser oidcUser = delegate.loadUser(request);
            // Process login: update login count, last login, etc.
            userService.processLogin(oidcUser);
            return oidcUser;
        };
    }
}
