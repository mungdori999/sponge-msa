package com.petweb.sponge.oauth2.dto;



import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class CustomOAuth2User  implements UserDetails {

    private final LoginAuth loginAuth;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    public Long getId() {
        return loginAuth.getId();
    }

    public String getLoginType() {
        return loginAuth.getLoginType();
    }
}
