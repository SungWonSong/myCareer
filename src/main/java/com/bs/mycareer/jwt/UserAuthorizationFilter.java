package com.bs.mycareer.jwt;

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


    public UserAuthorizationFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");

        
        if ((token != null) && (jwtUtil.isStartWithPrefix(token))) {
            token = jwtUtil.removePrefix(token);
        }

        // validate안하면 로그인페이지로 넘겨줘야된다... (수정필요)
        if (!jwtUtil.validateAccessToken(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;

        } else {
            httpServletResponse.addHeader("Authorization", "Bearer " + token);
            filterChain.doFilter(httpServletRequest, httpServletResponse);

        }
    }
}
//        # 이부분은 인증객체를 넘겨줘서 가져올때 사용하는 부분('세션')
//        String email = decodedJWT.getClaim("email").asString();
//        BSUserDetail bsUserDetail = bsUserDetailsService.loadUserByUsername(email);
//        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(bsUserDetail, bsUserDetail.getPassword(), bsUserDetail.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authenticated);



