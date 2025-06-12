package com.petweb.sponge.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petweb.sponge.exception.ResponseError;
import com.petweb.sponge.oauth2.dto.CustomOAuth2User;
import com.petweb.sponge.oauth2.dto.LoginAuth;
import com.petweb.sponge.utils.ResponseHttpStatus;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isSkipUri(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");

        if (isTokenMissing(token, request, response)) return;

        // GET요청이면서 token이 없을 수 있음
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!validateToken(token, response)) return;

        setAuthenticationContext(token);
        filterChain.doFilter(request, response);
    }

    /**
     * 인증 제외 URI 체크
     */
    private boolean isSkipUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.matches("^/api/auth(?:/.*)?$") ||
                (uri.equals("/api/trainer") || uri.equals("/api/trainer/image")) &&
                        request.getMethod().equalsIgnoreCase("POST");
    }

    /**
     * 토큰 없을 때 처리 단 GET요청은 예외
     */
    private boolean isTokenMissing(String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (token == null) {
            if (!request.getMethod().equalsIgnoreCase("GET")) {
                log.error("토큰이 없음");
                respondWithError(response, 401, "토큰이 없습니다.");
                return true;
            }
        }
        return false;
    }

    /**
     * 토큰 유효성 검사
     */
    private boolean validateToken(String token, HttpServletResponse response) {

        try {
            jwtUtil.isExpired(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("토큰 만료: {}", e.getMessage());
            respondWithError(response, ResponseHttpStatus.EXPIRE_ACCESS_TOKEN.getCode(), "토큰이 만료되었습니다.");
        } catch (SignatureException e) {
            log.error("토큰 위조: {}", e.getMessage());
            respondWithError(response, 401, "위조된 토큰입니다.");
        }
        return false;
    }

    /**
     * 스프링 시큐리티 인증 컨텍스트 설정
     */
    private void setAuthenticationContext(String token) {
        Long id = jwtUtil.getId(token);
        String loginType = jwtUtil.getLoginType(token);

        LoginAuth loginAuth = LoginAuth.builder()
                .id(id)
                .loginType(loginType)
                .build();

        CustomOAuth2User user = new CustomOAuth2User(loginAuth);
        Authentication authToken = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    /**
     * 응답 에러 출력
     */
    private void respondWithError(HttpServletResponse response, int status, String message) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResponseError error = new ResponseError(status, message);
        try {
            String json = new ObjectMapper().writeValueAsString(error);
            response.getWriter().write(json);
        } catch (IOException e) {
            log.error("Response write error", e);
        }
    }
}
