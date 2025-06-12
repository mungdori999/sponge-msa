package com.petweb.sponge.jwt;

import com.petweb.sponge.exception.ResponseError;
import com.petweb.sponge.utils.ResponseHttpStatus;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/reissue")
public class ReissueController {

    private final RefreshRepository refreshRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("")
    public ResponseEntity<?> reissue(@RequestBody RefreshToken token) {
        String refreshToken = token.getRefreshToken();

        //토큰이 존재하지 않는다면
        if (!refreshRepository.existsByRefresh(refreshToken)) {
            return new ResponseEntity<>(
                    new ResponseError(ResponseHttpStatus.EXPIRE_REFRESH_TOKEN.getCode(), "토큰이 만료되었습니다."),
                    HttpStatus.UNAUTHORIZED);
        }
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            refreshRepository.deleteByRefresh(refreshToken);
            return new ResponseEntity<>(
                    new ResponseError(ResponseHttpStatus.EXPIRE_REFRESH_TOKEN.getCode(), "토큰이 만료되었습니다."),
                    HttpStatus.UNAUTHORIZED);
        }
        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refreshToken")) {
            return new ResponseEntity<>(
                    new ResponseError(ResponseHttpStatus.EXPIRE_REFRESH_TOKEN.getCode(), "토큰이 만료되었습니다."),
                    HttpStatus.UNAUTHORIZED);
        }
        Long id = jwtUtil.getId(refreshToken);
        String name = jwtUtil.getName(refreshToken);
        String loginType = jwtUtil.getLoginType(refreshToken);
        Token newToken = jwtUtil.createToken(id, name, loginType);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefresh(refreshToken);

        refreshRepository.save(newToken.getRefreshToken());
        return ResponseEntity.ok().header("Authorization", newToken.getAccessToken())
                .body(new RefreshToken(newToken.getRefreshToken()));
    }
}
