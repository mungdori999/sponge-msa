package com.petweb.sponge.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petweb.sponge.exception.ResponseError;
import com.petweb.sponge.utils.ResponseHttpStatus;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class LogoutJwtFilter extends GenericFilterBean {
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // logout Uri가 아니라면 패스
        if (!isLogoutUri(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");


        if (isTokenMissing(token, request, response)) return;

        if (!validateToken(token, response)) return;

        String body = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        RefreshToken tokenDto = objectMapper.readValue(body, RefreshToken.class);
        String refreshToken = tokenDto.getRefreshToken();

        refreshRepository.deleteByRefresh(refreshToken);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    /**
     * 해당하는 URI 체크
     */
    private boolean isLogoutUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.equals("/api/logout") &&
                request.getMethod().equalsIgnoreCase("POST");
    }

    /**
     * 토큰 없을 때 처리
     */
    private boolean isTokenMissing(String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (token == null) {
            log.error("토큰 없음");
            respondWithError(response, 401, "토큰이 없습니다.");
            return true;

        }
        return false;
    }

    /**
     * 토큰 유효성 검사
     */
    private boolean validateToken(String token, HttpServletResponse response) throws IOException {
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
