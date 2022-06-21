package me.xstrixu.onlinegradebook.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import me.xstrixu.onlinegradebook.user.details.ManagementUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import static me.xstrixu.onlinegradebook.auth.JwtConstant.CLAIM_MANAGEMENT;
import static me.xstrixu.onlinegradebook.auth.JwtConstant.COOKIE_NAME;

@RequiredArgsConstructor
class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ManagementCredentials managementCredentials;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Optional<String> jwtOptional = getJwtFromRequest(request);

        if (jwtOptional.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        Optional<DecodedJWT> decodedJWTOptional = jwtUtil.validate(jwtOptional.get());

        if (decodedJWTOptional.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        DecodedJWT jwt = decodedJWTOptional.get();
        Collection<GrantedAuthority> authorities = jwtUtil.getAuthorities(jwt);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            boolean isManagementUser = jwt.getClaim(CLAIM_MANAGEMENT).asBoolean() != null;
            UserDetails userDetails = isManagementUser
                    ? new ManagementUserDetails(managementCredentials.username(), managementCredentials.password())
                    : userDetailsService.loadUserByUsername(jwt.getSubject());
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private Optional<String> getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    return Optional.of(cookie.getValue());
                }
            }
        }

        return Optional.empty();
    }
}
