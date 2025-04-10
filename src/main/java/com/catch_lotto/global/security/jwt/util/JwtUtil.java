package com.catch_lotto.global.security.jwt.util;

import com.catch_lotto.domain.user.dto.CustomUserDetails;
import com.catch_lotto.global.exception.CustomException;
import com.catch_lotto.global.response.ResponseCode;
import com.catch_lotto.global.util.RedisUtil;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessTokenExpirationMs;
    private final Long refreshTokenExpirationMs;
    private final RedisUtil redisUtil;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.access}") Long accessTokenExpirationMs,
                   @Value("${jwt.refresh}") Long refreshTokenExpirationMs, RedisUtil redisUtil) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
        this.redisUtil = redisUtil;
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // Token 발급
    public String tokenProvider(CustomUserDetails customUserDetails, Instant expiration) {
        Instant issuedAt = Instant.now();
        String authorities = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(customUserDetails.getUsername())
                .claim("role", authorities)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    public String createJwtAccessToken(CustomUserDetails customUserDetails) {
        Instant expiration = Instant.now().plusMillis(accessTokenExpirationMs);
        return tokenProvider(customUserDetails, expiration);
    }

    public String createJwtRefreshToken(CustomUserDetails customUserDetails) {
        Instant expiration = Instant.now().plusMillis(refreshTokenExpirationMs);
        String refreshToken = tokenProvider(customUserDetails, expiration);

        // 레디스에 저장
        redisUtil.save(
                customUserDetails.getUsername(),
                refreshToken,
                refreshTokenExpirationMs,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    public boolean validateRefreshToken(String refreshToken) {
        // 토큰에서 사용자 ID 추출
        String username = getSubject(refreshToken);

        // Redis에서 해당 사용자의 refreshToken 조회
        if (!redisUtil.hasKey(username)) {
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        }

        return true;
    }

    // HTTP 요청의 'Authorization' 헤더에서 JWT 액세스 토큰을 검색
    public String resolveAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.isBlank()) {
            log.warn("[*] Authorization header is missing");
            return null;
        }

        if (!authorization.startsWith("Bearer ")) {
            log.warn("[*] Authorization header does not start with 'Bearer '");
            return null;
        }

        String[] parts = authorization.split(" ");
        if (parts.length != 2 || parts[1].isBlank()) {
            log.warn("[*] Malformed Authorization header");
            return null;
        }

        log.info("[*] Token extracted successfully");
        return parts[1];
    }

    public void validateTokenOrThrow(String token) {
        try {
            Jwts.parser()
                    .clockSkewSeconds(180)
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (SecurityException | MalformedJwtException e) {
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ResponseCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(ResponseCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }
    }

    public long getAccessTokenRemainingTime(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.getTime() - System.currentTimeMillis();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey) // jjwt 0.12.3 기준
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 쿠키
    public Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refreshToken", value);
        cookie.setMaxAge(24*60*60);
        // https 설정 시
//        cookie.setSecure(true);
//        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
