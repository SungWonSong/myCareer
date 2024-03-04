package com.bs.mycareer.Common.jwt;

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

        String requestURI = httpServletRequest.getRequestURI();

        // "/register" 요청일 경우 토큰 검증을 건너뜁니다.
        if ("/register".equals(requestURI) || "/login".equals(requestURI) || "/career/ContentLists".equals(requestURI) ||"/career/create".equals(requestURI)
                || "/".equals(requestURI) || requestURI.startsWith("/css/") || requestURI.startsWith("/js/")
                || requestURI.startsWith("/images/") || requestURI.matches("^/career/[0-9]+$")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }


        String token = httpServletRequest.getHeader("Authorization");


        if ((token != null) && (jwtUtil.isStartWithPrefix(token))) {
            token = jwtUtil.removePrefix(token);
        }


        // validate안하면 로그인페이지로 넘겨줌 - 문제점 : career_id값으로 들어오는 그걸 안거쳐야된다. / 없애는게 맞나 ?
        if (!jwtUtil.validateAccessToken(token)) {
            jwtUtil.destroyAccessToken(httpServletRequest, httpServletResponse);

            String loginPage = "/register";

            httpServletResponse.setStatus(HttpServletResponse.SC_SEE_OTHER);
            httpServletResponse.setHeader("Location", loginPage);

            // 여기서 로그아웃을 시킨다음에 자동으로 로그인페이지로 보내는거야...

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



