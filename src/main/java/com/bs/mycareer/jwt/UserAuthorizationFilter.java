package com.bs.mycareer.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class UserAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private final JWTUtil jwtUtil;

//    @Autowired
//    private final BSUserDetailsService bsUserDetailsService;


    public UserAuthorizationFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");
        System.out.println("token 요기요기= " + token);
        if (token == null || !jwtUtil.isStartWithPrefix(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        if (token.contains("Bearer ")) {
            token = jwtUtil.removePrefix(token);
        }

        DecodedJWT decodedJWT = jwtUtil.verifyAccessToken(token);


        httpServletResponse.addHeader("Authorization", "Bearer " + decodedJWT);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
//        # 이부분은 인증객체를 넘겨줘서 가져올때 사용하는 부분('세션')
//        String email = decodedJWT.getClaim("email").asString();
//        BSUserDetail bsUserDetail = bsUserDetailsService.loadUserByUsername(email);
//        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(bsUserDetail, bsUserDetail.getPassword(), bsUserDetail.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authenticated);



