package com.petweb.sponge.auth;

import com.petweb.sponge.oauth2.dto.CustomOAuth2User;
import com.petweb.sponge.oauth2.dto.LoginAuth;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String idHeader = request.getHeader("X-Id");
        String loginType = request.getHeader("X-LoginType");
        if (idHeader != null && loginType != null) {
            Long id = Long.valueOf(idHeader);
            LoginAuth loginAuth = LoginAuth.builder()
                    .id(id)
                    .loginType(loginType)
                    .build();
            CustomOAuth2User user = new CustomOAuth2User(loginAuth);
            Authentication authToken
                    = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        }
        filterChain.doFilter(request, response);
    }
}
