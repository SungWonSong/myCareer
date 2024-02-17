package com.bs.mycareer.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.bs.mycareer.User.dto.AuthenticationResponse;
import com.bs.mycareer.User.dto.BSUserDetail;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;


@Component //jwt 0.12.3 버전 사용 (타 블로그: 0.11.5)
public class JWTUtil {
    private static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L;  // 토큰 만료시간: (60 * 1000L 이 1분) -> access token은 1시간
    private static final long REFRESH_TOKEN_TIME = 2 * 7 * 24 * 60 * 60 * 1000L; // -> refresh token은 2주


    @Autowired
    private final JWTProperties jwTproperties;

    @Autowired
    private final AuthenticationResponse authenticationResponse;

    public JWTUtil(JWTProperties jwTproperties, AuthenticationResponse authenticationResponse) {
        this.jwTproperties = jwTproperties;
        this.authenticationResponse = authenticationResponse;
    }

    //SecretKey라는 밑 코드를 통해 객체화 된 키를 사용(HS256는 대표적인 대칭 키 방식이며, 한 번 생성된 키를 애플리케이션 내에서만 사용) -> access refresh 나누기


//    public String resolveAccessToken(HttpServletRequest httpServletRequest) {
//        // Authorization 헤더에서 토큰을 추출
//        String authorizationHeader = httpServletRequest.getHeader("Authorization");
//
//        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
//            // "Bearer " 이후의 문자열이 토큰이므로 추출
//            return authorizationHeader.substring(7);
//        }
//
//        return null;

    // Access_token 생성 로직 ( 생성자를 넣어줘서 verify때 검증하면 좀더 높게 평가 )
    public String createAccessToken(BSUserDetail bsUserDetail) {
        Date date = new Date();
        return Jwts.builder()
                .claim("subject", "ACCESS_TOKEN")
                .claim("email", bsUserDetail.getUser().getEmail())
                .claim("authority", bsUserDetail.getAuthorities())
                .issuedAt(date)
                .expiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                .signWith(jwTproperties.getAccessSecretKey())
                .compact();

        // compact()메서드 호출하여 압축된 JWT문자열 얻음
    }
    public String generateAccessToken(BSUserDetail bsUserDetail) {
        return createAccessToken(bsUserDetail);
    }


    // Refresh_token 생성 로직
    public String createRefreshToken(BSUserDetail bsUserDetail) {
        Date date = new Date();
        return Jwts.builder()
                .claim("subject", "REFRESH_TOKEN")
                .claim("email", bsUserDetail.getUser().getEmail())
                .claim("authority", bsUserDetail.getAuthorities())
                .issuedAt(date)
                .expiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .signWith(jwTproperties.getRefreshSecretKey())
                .compact();// compact()메서드 호출하여 압축된 JWT문자열 얻음
    }

    public String generateRefreshToken(BSUserDetail bsUserDetail) {
        return createRefreshToken(bsUserDetail);
    }

    public DecodedJWT decodeToken(String token) {
        // Access 토큰인 경우
        if (isAccessToken(token)) {
            return JWT
                    .require(jwTproperties.getAccessSign())
                    .build()
                    .verify(token);
        }
        // Refresh 토큰인 경우
        else if (isRefreshToken(token)) {
            return JWT
                    .require(jwTproperties.getRefreshSign())
                    .build()
                    .verify(token);
        }
        throw new IllegalArgumentException("Invalid token");
    }


    // 추후에 expired를 오늘 날짜가 넘어가면 새로운 access token 발급 로직 작성 예정
    public DecodedJWT verifyAccessToken(String token) {
        try {
            Algorithm algorithm = jwTproperties.getAccessSign();
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("subject", "ACCESS_TOKEN")
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception){
            throw new IllegalStateException("Invalid token");
        }
    }

    // 이건 redis 연동 예정...
    public Optional<DecodedJWT> verifyRefreshToken(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        if (!decodedJWT.getToken().equals(authenticationResponse.refreshToken())) {
            throw new IllegalStateException("Invalid token");
        }
        return Optional.of(decodedJWT);

    }
        //Optional<UserRefreshToken> refreshToken = tokenRepository.findById(decodedJWT.getClaim("id").asString());
        //return refreshToken.filter(userRefreshToken -> userRefreshToken.refreshToken().equals(token)).map(userRefreshToken -> decodedJWT);

    public boolean isStartWithPrefix(String token) {
        return token.startsWith(jwTproperties.getPrefix());
    }

    public String removePrefix(String token){
        return token.substring(jwTproperties.getPrefix().length());
    }

    public Boolean isAccessToken(String token) {
        if (isStartWithPrefix(token)) {
            token = removePrefix(token);
        }

        DecodedJWT jwt;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
        String subject = jwt.getSubject();
        return subject != null && subject.trim().equals("ACCESS_TOKEN");
    }


    public Boolean isRefreshToken(String token){
        if (isStartWithPrefix(token)) {
            token = removePrefix(token);
        }
        DecodedJWT jwt;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
        String subject = jwt.getSubject();
        return subject != null && subject.trim().equals("REFRESH_TOKEN");
    }

}

//        - claim("role",bsUserDetail.getAuthorities().iterator().next()) 이걸로 대체
//        iterator 함수는 Collection에서 컬렉션 내의 요소를 순차적으로 접근할 때 편리하고 일관된 방법을 제공하기 때문
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        //iterator.next()는 현재 인증에서의 권한을 호출하고 다음번에 호출할때에는 다음순서로 넘기기에 자동 시퀀스 기능이라 생각하면 편하다.
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//
//        //role값을 추후 enum으로 해서 -> ADMIN, USER로 지정 이유 : 다른값이 들어오는거 방지 및 장점 보유...
//        String role = auth.getAuthority();


//// 토큰을 생성하고 검증하는 클래스입니다.
//// 해당 컴포넌트는 필터클래스에서 사전 검증을 거칩니다.
//@RequiredArgsConstructor
//@Component
//public class JwtTokenProvider {
//    private String secretKey = "myprojectsecret";
//
//    // 토큰 유효시간 30분
//    private long tokenValidTime = 30 * 60 * 1000L;
//
//    private final UserDetailsService userDetailsService;
//
//    // 객체 초기화, secretKey를 Base64로 인코딩한다.
//    @PostConstruct
//    protected void init() {
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//    }
//
//    // JWT 토큰 생성
//    public String createToken(String userPk, List<String> roles) {
//        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
//        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims) // 정보 저장
//                .setIssuedAt(now) // 토큰 발행 시간 정보
//                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
//                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
//                // signature 에 들어갈 secret값 세팅
//                .compact();
//    }
//
//    // JWT 토큰에서 인증 정보 조회
//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
//
//    // 토큰에서 회원 정보 추출
//    public String getUserPk(String token) {
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
//    }
//
//    // Request의 Header에서 token 값을 가져옵니다. "Authorization" : "TOKEN값'
//    public String resolveToken(HttpServletRequest request) {
//        return request.getHeader("Authorization");
//    }
//
//    // 토큰의 유효성 + 만료일자 확인
//    public boolean validateToken(String jwtToken) {
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
// 블로그에 하나의 코드 설명 작성(보고이해바람), 메서드 체인 방식 이용 : 메서드 연쇄적 호출하여 간결하고 가독성이 좋은 코드 작성
//    public String getSubject(String token) {
//
////        return Jwts.parser().verifyWith((SecretKey) accessSecretKey).build().parseSignedClaims(token).getPayload().getSubject();
////    }
//
//    public String getEmail(String token) {
//
//        return Jwts.parser().verifyWith((SecretKey) accessSecretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
//    }
//    public String getRole(String token) {
//
//        return Jwts.parser().verifyWith((SecretKey) accessSecretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
//    }
//
////    public Boolean BeExpired(String token) {
////
////        return Jwts.parser().verifyWith((SecretKey) accessSecretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
////    }