package com.petweb.sponge.config;

import com.petweb.sponge.auth.AuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());


        //JWTFilter 추가
        http
                .addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.GET, "/api/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user")
                        .permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/user/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/trainer")
                        .permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/trainer/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/trainer/image")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                );


        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }

}