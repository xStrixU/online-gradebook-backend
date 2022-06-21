package me.xstrixu.onlinegradebook.auth;

import lombok.RequiredArgsConstructor;
import me.xstrixu.onlinegradebook.user.details.ManagementUserDetails;
import me.xstrixu.onlinegradebook.user.dto.ManagementLoginUserDto;
import me.xstrixu.onlinegradebook.user.dto.UserLoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@RequiredArgsConstructor
public class AuthFacade {

    private final JwtConfigurer jwtConfigurer;
    private final JwtUtil jwtUtil;
    private final ManagementCredentials managementCredentials;

    private AuthenticationManager authenticationManager;

    @Lazy
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void configure(HttpSecurity http) throws Exception {
        http.apply(jwtConfigurer);
    }

    public void login(HttpServletResponse response, UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword())
        );
        String jwt = jwtUtil.generate(authentication.getName(), authentication.getAuthorities(), false);

        jwtUtil.addJwtCookie(response, jwt);
    }

    public void managementLogin(HttpServletResponse response, ManagementLoginUserDto managementLoginUserDto) {
        if (!managementCredentials.usernamePasswordEquals(managementLoginUserDto.getUsername(), managementLoginUserDto.getPassword())) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        String jwt = jwtUtil.generate(managementLoginUserDto.getUsername(), Collections.singleton(ManagementUserDetails.AUTHORITY), true);

        jwtUtil.addJwtCookie(response, jwt);
    }

    public void logout(HttpServletResponse response) {
        jwtUtil.deleteJwtCookie(response);
    }
}
