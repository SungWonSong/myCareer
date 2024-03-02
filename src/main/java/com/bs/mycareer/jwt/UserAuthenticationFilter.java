package com.bs.mycareer.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bs.mycareer.User.dto.AccessTokenResponse;
import com.bs.mycareer.User.dto.AuthenticationRequest;
import com.bs.mycareer.User.dto.BSUserDetail;
import com.bs.mycareer.User.dto.RefreshTokenResponse;
import com.bs.mycareer.User.service.BSUserDetailsService;
import com.bs.mycareer.utils.JsonUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


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

        HttpSession session = httpServletRequest.getSession();
        AuthenticationRequest authenticationRequest = JsonUtil.readValue(httpServletRequest, AuthenticationRequest.class);

        // 밑에 두가지 세션의 attribute된것들은 불러와진다..... 존재 안한다 생각하면 안된다.
        AccessTokenResponse accessTokenResponse = (AccessTokenResponse) session.getAttribute("savedAccessToken");
        RefreshTokenResponse refreshTokenResponse = (RefreshTokenResponse) session.getAttribute("savedRefreshToken");

        // 첫로그인일때 .... (수정요소) -> 현재 saved이게 각 회원마다 만들어지는지 아니면 공통으로 만들어지는지를 인지하고 파악필요
        if (accessTokenResponse == null && refreshTokenResponse == null) {
            UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unauthenticated(authenticationRequest.email(), authenticationRequest.password());
            return authenticationManager.authenticate(authentication);

        } else if (refreshTokenResponse.refreshToken() == null || !jwtUtil.validateRefreshToken(refreshTokenResponse.refreshToken())) {
            accessTokenResponse.accessToken(null);
            refreshTokenResponse.refreshToken(null);
            UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unauthenticated(authenticationRequest.email(), authenticationRequest.password());
            return authenticationManager.authenticate(authentication);

        } else if (accessTokenResponse.accessToken() == null && jwtUtil.validateAccessToken(refreshTokenResponse.refreshToken())){
            BSUserDetail bsUserDetail = null;
            try {
                DecodedJWT decodedJWT = jwtUtil.verifyRefreshToken(refreshTokenResponse.refreshToken());
                String email = decodedJWT.getClaim("email").asString();
                bsUserDetail = bsUserDetailsService.loadUserByUsername(email);
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to retrieve user details");
            }
            String accessToken = jwtUtil.generateAccessToken(bsUserDetail);
            accessTokenResponse.accessToken(accessToken);
            httpServletResponse.addHeader("Authorization", "Bearer " + accessToken);
        } else {
            String access = accessTokenResponse.accessToken();
            httpServletResponse.addHeader("Authorization", "Bearer " + access);
        }
        throw new AuthenticationServiceException("Authentication failed");
    }

            // 첫로그인시 비인증객체를 넘겨서 successful 로직으로 넘어감


//        } else {   //refresh 토큰이 존재하고 검증되면 access 토큰 재발급 ()
//            Optional<DecodedJWT> verifyToken = jwtUtil.verifyRefreshToken(authenticationRequest.refreshToken());
//            DecodedJWT decodedJWT = verifyToken.orElseThrow(() -> new IllegalArgumentException("INVALID TOKEN"));
//            BSUserDetail bsUserDetail = (BSUserDetail) bsUserDetailsService.loadUserByUsername(decodedJWT.getClaim("email").asString());
//            return UsernamePasswordAuthenticationToken.authenticated(bsUserDetail, null, bsUserDetail.getAuthorities());
//        }


    //로그인 성공시 자동 실행되는 메소드 (여기서 access,refresh 토큰 발행), userdetail를 넣는이유 : chatgpt마지막 작성... 토대로 이해하기

    @Override
    protected void successfulAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain chain, Authentication authentication) throws IOException {
        BSUserDetail bsUserDetail = (BSUserDetail) authentication.getPrincipal();

        String accessToken = jwtUtil.generateAccessToken(bsUserDetail);
        String refreshToken = jwtUtil.generateRefreshToken(bsUserDetail);

        httpServletResponse.addHeader("Authorization", "Bearer " + accessToken);

        //accesstoken, refreshtoken 객체로 만들어 저장
        HttpSession session = httpServletRequest.getSession(false);
        
        if (session.getAttribute("savedAccessToken") == null) {
            AccessTokenResponse savedAccessToken = new AccessTokenResponse();
            savedAccessToken.accessToken(accessToken);
            session.setAttribute("savedAccessToken", savedAccessToken);
            JsonUtil.writeValue(httpServletResponse.getOutputStream(), savedAccessToken);

        } else if (session.getAttribute("savedAccessToken") != null) {
            AccessTokenResponse savedAccessToken = (AccessTokenResponse) session.getAttribute("savedAccessToken");
            savedAccessToken.accessToken(accessToken);
            JsonUtil.writeValue(httpServletResponse.getOutputStream(), savedAccessToken);
        }

        if (session.getAttribute("savedRefreshToken") == null) {
            RefreshTokenResponse savedRefreshTokenNew = new RefreshTokenResponse().refreshToken(refreshToken);
            session.setAttribute("savedRefreshToken", savedRefreshTokenNew);
        } else if (session.getAttribute("savedRefreshToken") != null) {
            RefreshTokenResponse savedRefreshToken = (RefreshTokenResponse) session.getAttribute("savedRefreshToken");
            if (savedRefreshToken.refreshToken() == null) {
                savedRefreshToken.refreshToken(refreshToken);
            }
        }
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


//// iterator 함수는 Collection에서 컬렉션 내의 요소를 순차적으로 접근할 때 편리하고 일관된 방법을 제공하기 때문
//Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
////iterator.next()는 현재 인증에서의 권한을 호출하고 다음번에 호출할때에는 다음순서로 넘기기에 자동 시퀀스 기능이라 생각하면 편하다.
//Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//GrantedAuthority auth = iterator.next();
//
////role값을 추후 enum으로 해서 -> ADMIN, USER로 지정 이유 : 다른값이 들어오는거 방지 및 장점 보유...
//String role = auth.getAuthority();
//
