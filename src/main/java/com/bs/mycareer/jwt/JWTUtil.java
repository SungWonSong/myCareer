package com.bs.mycareer.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component //jwt 0.12.3 버전 사용 (타 블로그: 0.11.5)
public class JWTUtil {

    private SecretKey secretKey;

    //SecretKey라는 밑 코드를 통해 객체화 된 키를 사용(HS256는 대표적인 대칭 키 방식이며, 한 번 생성된 키를 애플리케이션 내에서만 사용)
    public JWTUtil(@Value("${spring.jwt.bs}")String secret) {


        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
    // 블로그에 하나의 코드 설명 작성(보고이해바람), 메서드 체인 방식 이용 : 메서드 연쇄적 호출하여 간결하고 가독성이 좋은 코드 작성
    public String getEmail(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    //String role은 결국 일단 user만이 존재하기에 role:user로 통일
    public String createJwt(String email, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact(); // compact()메서드 호출하여 압축된 JWT문자열 얻음
    }
}

