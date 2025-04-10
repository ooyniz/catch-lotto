package com.catch_lotto.global.security.jwt;

import com.catch_lotto.domain.user.dto.CustomUserDetails;
import com.catch_lotto.global.util.RedisUtil;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
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

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
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

    public String createJWT(String category, String username, String role) {
        Long expiredMs = category.equals("access") ? accessTokenExpirationMs : refreshTokenExpirationMs;

        return Jwts.builder()
                .subject(username)
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
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
            return false; // todo: throw
        }

        return true;
    }

    // HTTP 요청의 'Authorization' 헤더에서 JWT 액세스 토큰을 검색
    public String resolveAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.warn("[*] No Token in req");
            return null;
        }
        log.info("[*] Token exists");

        return authorization.split(" ")[1];
    }

    public void validateToken(String token) {
        try {
            // 구문 분석 시스템의 시계가 JWT를 생성한 시스템의 시계 오차 고려
            // 약 3분 허용.
            long seconds = 3 *60;
            boolean isExpired = Jwts
                    .parser()
                    .clockSkewSeconds(seconds)
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
            log.info("[*] Authorization with Token");
            if (isExpired) {
                log.info("만료된 JWT 토큰입니다.");
            }
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
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
}
