package com.bs.mycareer.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bs.mycareer.User.dto.AuthenticationRequest;
import com.bs.mycareer.User.dto.AuthenticationResponse;
import com.bs.mycareer.User.dto.BSUserDetail;
import com.bs.mycareer.User.service.BSUserDetailsService;
import com.bs.mycareer.utils.JsonUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Optional;


public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final JWTUtil jwtUtil;

    @Autowired
    private final BSUserDetailsService bsUserDetailsService;


    // 생성자 주입 - Configuration
    public UserAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, BSUserDetailsService bsUserDetailsService) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.bsUserDetailsService = bsUserDetailsService;
    }

    // filter에서 로그인을 한다면 header에 json형식으로 Authorization(본문) 조작 - Javascript / fetch-api
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        AuthenticationRequest authenticationRequest = JsonUtil.readValue(httpServletRequest, AuthenticationRequest.class);

        //refresh 토큰이 null or 비어있을경우, 비인증 토큰으로 만들어서 인증을 보내서 -> success 로직 실행
//        if (authenticationRequest.refreshToken() == null || authenticationRequest.refreshToken().isEmpty()) {
            //UsernamePasswordAuthenticationToken 이거 Costom은 이번엔 하지 않는걸로....
            UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unauthenticated(authenticationRequest.email(), authenticationRequest.password());
            return authenticationManager.authenticate(authentication);
//        } else {   //refresh 토큰이 존재하고 검증되면 access 토큰 재발급
//            Optional<DecodedJWT> verifyToken = jwtUtil.verifyRefreshToken(authenticationRequest.refreshToken());
//            DecodedJWT decodedJWT = verifyToken.orElseThrow(() -> new IllegalArgumentException("INVALID TOKEN"));
//            BSUserDetail bsUserDetail = (BSUserDetail) bsUserDetailsService.loadUserByUsername(decodedJWT.getClaim("email").asString());
//            return UsernamePasswordAuthenticationToken.authenticated(bsUserDetail, null, bsUserDetail.getAuthorities());
//        }
    }

    //로그인 성공시 자동 실행되는 메소드 (여기서 access,refresh 토큰 발행), userdetail를 넣는이유 : chatgpt마지막 작성... 토대로 이해하기
    @Override
    protected void successfulAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain chain, Authentication authentication) {
        BSUserDetail bsUserDetail = (BSUserDetail) authentication.getPrincipal();

        String accessToken = jwtUtil.generateAccessToken(bsUserDetail);
        String refreshToken = jwtUtil.generateRefreshToken(bsUserDetail);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse().accessToken(accessToken).refreshToken(refreshToken);
        // refresh token은 redis에 저장 / accesstoken은 header에 담아서 반환해서 저장 x
        httpServletResponse.addHeader("Authorization", "Bearer " + accessToken);

    }


    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException failed) {
        httpServletResponse.setStatus(401);
    }
}


    // 새로추가한거라 @override x -> 원래 없던 로직 / email은 null or @포함하고 있지않으면 예외처리 구문을 던지고, 아니면 return email을 한다.
//    protected String obtainUserEmail(HttpServletRequest httpServletRequest) {
//        String email = httpServletRequest.getParameter("email");
//        if (email == null || !email.contains("@")) {
//            throw new IllegalArgumentException("알맞지 않은 이메일 형식입니다");
//        }
//        return email;
//    }
//}

//    protected String obtainUserRole(HttpServletRequest httpServletRequest) {
//        String role = httpServletRequest.getParameter("role");
//        if (role == null) {
//            throw new IllegalArgumentException("롤이 선택되지 않았습니다");
//        }
//        return role;
//    }
//@Override
//public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//    AuthenticationRequest authenticationRequest = JsonUtil.readValue(request, AuthenticationRequest.class);
//
//    if(StringUtil.isEmpty(authenticationRequest.refreshToken())){
//        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(authenticationRequest.id(), authenticationRequest.password());
//        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//        return authenticate;
//    }else{
//        Optional<DecodedJWT> verifyToken = jwtUtil.verifyRefreshToken(authenticationRequest.refreshToken());
//        DecodedJWT decodedJWT = verifyToken.orElseThrow(() -> new InvalidTokenException("INVALID TOKEN"));
//        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getClaim("id").asString());
//        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());
//        return authenticationToken;
//    }
// typecast 방법
//BSUserDetail bsUserDetail = (BSUserDetail) authentication.getPrincipal();
//String email = bsUserDetail.getUsername();
//
//// iterator 함수는 Collection에서 컬렉션 내의 요소를 순차적으로 접근할 때 편리하고 일관된 방법을 제공하기 때문
//Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
////iterator.next()는 현재 인증에서의 권한을 호출하고 다음번에 호출할때에는 다음순서로 넘기기에 자동 시퀀스 기능이라 생각하면 편하다.
//Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//GrantedAuthority auth = iterator.next();
//
////role값을 추후 enum으로 해서 -> ADMIN, USER로 지정 이유 : 다른값이 들어오는거 방지 및 장점 보유...
//String role = auth.getAuthority();
//
//String accessToken = jwtUtil.generateAccessToken(email, role);
//String refreshToken = jwtUtil.generateRefreshToken(email, role);
//
//AuthenticationResponse authenticationResponse = new AuthenticationResponse().accessToken(accessToken).refreshToken(refreshToken);
//// refresh token은 현재 dto에 저장을 해놓지만 원래 db에서 꺼내서 유효하면 그걸 가지고 access 발급
//        httpServletResponse.addHeader("Authorization", "Bearer " + authenticationResponse.accessToken());
//
//        }
