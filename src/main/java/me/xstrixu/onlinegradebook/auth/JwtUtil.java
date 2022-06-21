package me.xstrixu.onlinegradebook.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static me.xstrixu.onlinegradebook.auth.JwtConstant.*;

class JwtUtil {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    JwtUtil(String secret) {
        algorithm = Algorithm.HMAC256(secret);
        verifier = JWT.require(algorithm).build();
    }

    Optional<DecodedJWT> validate(String jwt) {
        return Optional.ofNullable(verifier.verify(jwt));
    }

    String generate(String subject, Collection<? extends GrantedAuthority> grantedAuthorities, boolean isManagement) {
        String authorities = grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        JWTCreator.Builder builder = JWT.create()
                .withSubject(subject)
                .withClaim(CLAIM_AUTHORITIES, authorities);

        if (isManagement) {
            builder.withClaim(CLAIM_MANAGEMENT, true);
        }

        return builder.sign(algorithm);
    }

    Collection<GrantedAuthority> getAuthorities(Payload payload) {
        return Arrays.stream(payload.getClaim(CLAIM_AUTHORITIES).asString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    void addJwtCookie(HttpServletResponse response, String jwt) {
        addCookie(response, jwt, 60 * 60);
    }

    void deleteJwtCookie(HttpServletResponse response) {
        addCookie(response, "logoutxdddd", 0);
    }

    private void addCookie(HttpServletResponse response, String value, int maxAge) {
        var cookie = new Cookie(COOKIE_NAME, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }
}
