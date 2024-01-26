package com.bs.mycareer.jwt;

import com.bs.mycareer.dto.CommonUserDetailDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;


public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    // 생성자 주입 - Configuration
    public UserAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {

        //클라이언트 요청에서 email, password 추출
        String email = obtainUserEmail(httpServletRequest);
        String password = obtainPassword(httpServletRequest);

        //스프링 시큐리티에서 email과 password를 검증하기 위해서는 token에 담아야 함 (아직 role이 없기에 null로 설정)
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain chain, Authentication authentication) {
        CommonUserDetailDto commonUserDetailDto = (CommonUserDetailDto) authentication.getAuthorities();
        String email = commonUserDetailDto.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(email, role, 60 * 60 * 10L);

        httpServletResponse.addHeader("Authorization", "Bearer " + token);

    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException failed) {
        httpServletResponse.setStatus(401);
    }


    // 새로추가한거라 @override x -> 원래 없던 로직 / email은 null or @포함하고 있지않으면 예외처리 구문을 던지고, 아니면 return email을 한다.
    protected String obtainUserEmail(HttpServletRequest httpServletRequest) {
        String email = httpServletRequest.getParameter("email");
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("알맞지 않은 이메일 형식입니다");
        }
        return email;
    }
}

//    protected String obtainUserRole(HttpServletRequest httpServletRequest) {
//        String role = httpServletRequest.getParameter("role");
//        if (role == null) {
//            throw new IllegalArgumentException("롤이 선택되지 않았습니다");
//        }
//        return role;
//    }

