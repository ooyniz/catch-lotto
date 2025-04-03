package com.catch_lotto.global.security.jwt;

//@Service
//public class JwtTokenProvider {
//    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
//
//    private final long expiration;
//    private final Key SECRET_KEY;
//
//    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") long expiration){
//        this.expiration = expiration;
//        this.SECRET_KEY = Keys.hmacShaKeyFor(getDecoder().decode(secretKey));
//    }
//
//    public String createToken(String username, Role role) {
//        Claims claims = Jwts.claims().setSubject(username);
//        claims.put("role", role != null ? role.getKey() : "ROLE_USER");
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime() + expiration * 1000))
//                .signWith(SECRET_KEY)
//                .compact();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public Claims validateAndGetClaims(String token) {
//        try {
//            return Jwts.parserBuilder()
//                    .setSigningKey(SECRET_KEY)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (Exception e) {
//            logger.error("JWT 검증 실패: {}", e.getMessage());
//            throw new AuthenticationServiceException("Invalid JWT token", e);
//        }
//    }
//
//}
