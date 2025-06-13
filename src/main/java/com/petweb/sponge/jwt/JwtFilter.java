package com.petweb.sponge.jwt;

import com.petweb.sponge.oauth2.dto.CustomOAuth2User;
import com.petweb.sponge.oauth2.dto.LoginAuth;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Long id = Long.valueOf(request.getHeader("X-Id"));
        String loginType = request.getHeader("X-LoginType");

        LoginAuth loginAuth = LoginAuth.builder()
                .id(id)
                .loginType(loginType)
                .build();
        filterChain.doFilter(request, response);

        CustomOAuth2User user = new CustomOAuth2User(loginAuth);
        Authentication authToken = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
