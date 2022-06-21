package me.xstrixu.onlinegradebook.user.details;

import me.xstrixu.onlinegradebook.user.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public record ManagementUserDetails(String username, String password) implements UserDetails {

    public static final GrantedAuthority AUTHORITY = new SimpleGrantedAuthority("ROLE_" + UserRole.MANAGEMENT);

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(AUTHORITY);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
