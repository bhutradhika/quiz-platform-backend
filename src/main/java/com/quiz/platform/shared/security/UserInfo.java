package com.quiz.platform.shared.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.quiz.platform.feature_user.entities.postgres.User;
import com.quiz.platform.feature_user.entities.postgres.UserRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@JsonDeserialize(as = UserInfo.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo implements UserDetails {
    private transient TokenPayload tokenPayload;
    private transient Collection<? extends GrantedAuthority> authorities;
    private transient User user;

    public UserInfo() {}

    public UserInfo(User user) {
        this.user = user;
        this.tokenPayload = new TokenPayload(
            user.getId(),
            user.getEmail(),
            user.getUsername(),
            user.getRole()
        );
    }

    public UserInfo(TokenPayload tokenPayload) {
        this.tokenPayload = tokenPayload;
    }

    /**
     * Gets the user id from token payload or user object.
     *
     * @return the user id
     */
    public String getUserId() {
        if (user != null) {
            return user.getId();
        }
        return this.tokenPayload.getUserId();
    }

    /**
     * Gets the email from token payload or user object.
     *
     * @return the email
     */
    public String getEmail() {
        if (user != null) {
            return user.getEmail();
        }
        return this.tokenPayload.getEmail();
    }

    /**
     * Gets the username from token payload or user object.
     *
     * @return the username
     */
    public String getUsername() {
        return this.tokenPayload.getEmail();
    }

    /**
     * Gets the role from token payload or user object.
     *
     * @return the role
     */
    public UserRole getRole() {
        if (user != null) {
            return user.getRole();
        }
        return this.tokenPayload.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        if (tokenPayload != null && tokenPayload.getRole() != null) {
            authorityList.add(new SimpleGrantedAuthority("ROLE_" + tokenPayload.getRole().name()));
        }
        this.authorities = authorityList;
        return authorityList;
    }

    @Override
    public String getPassword() {
        return user != null ? user.getPassword() : null;
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
