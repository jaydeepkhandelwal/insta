package com.oci.insta.security;

import com.oci.insta.entities.models.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Data
public class InstaUserDetails implements OAuth2User,UserDetails {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public InstaUserDetails(Long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public static InstaUserDetails create(User user) {
        return new InstaUserDetails(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getEmail()
        );
    }

    public static InstaUserDetails create(User user, Map<String, Object> attributes) {
        InstaUserDetails instaUserDetails = InstaUserDetails.create(user);
        instaUserDetails.setAttributes(attributes);
        return instaUserDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
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

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
