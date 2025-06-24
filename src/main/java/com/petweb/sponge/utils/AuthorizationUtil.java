package com.petweb.sponge.utils;

import com.petweb.sponge.oauth2.dto.CustomUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUtil {

    public Long getLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getClass() == AnonymousAuthenticationToken.class) {
            return null;
        }
        CustomUser userDetails = (CustomUser) authentication.getPrincipal();

        return userDetails.getId();
    }

    public String getLoginType() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getClass() == AnonymousAuthenticationToken.class) {
            return null;
        }
        CustomUser userDetails = (CustomUser) authentication.getPrincipal();
        return userDetails.getLoginType();
    }

}
