package me.xstrixu.onlinegradebook.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
class AuthConfiguration {

    @Value("${app.security.jwt.secret}")
    private String secret;

    @Value("${app.management.username}")
    private String managementUsername;

    @Value("${app.management.password}")
    private String managementPassword;

    @Bean
    AuthFacade authFacade(UserDetailsService userDetailsService) {
        var jwtUtil = new JwtUtil(secret);
        var managementCredentials = new ManagementCredentials(managementUsername, managementPassword);
        var jwtFilter = new JwtFilter(jwtUtil, managementCredentials, userDetailsService);
        var jwtConfigurer = new JwtConfigurer(new JwtAuthenticationEntryPoint(), jwtFilter);

        return new AuthFacade(jwtConfigurer, jwtUtil, managementCredentials);
    }
}
