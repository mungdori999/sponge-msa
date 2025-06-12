package com.petweb.sponge.jwt;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    @Value("${spring.jwt.accessToken-expire-length}")
    private long accessExpireLong;
    @Value("${spring.jwt.refresh-expire-length}")
    private long refreshExpireLong;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public Long getId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", Long.class);
    }
    public String getName(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("name",String.class);
    }
    public String getLoginType(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("loginType", String.class);
    }
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category",String.class);
    }


    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public Token createToken(Long id, String name, String loginType) {
        String accessToken = Jwts.builder()
                .claim("id", id)
                .claim("name", name)
                .claim("loginType", loginType)
                .claim("category","accessToken")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpireLong))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .claim("id", id)
                .claim("name", name)
                .claim("loginType", loginType)
                .claim("category","refreshToken")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpireLong))
                .signWith(secretKey)
                .compact();
        return Token.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }
}
