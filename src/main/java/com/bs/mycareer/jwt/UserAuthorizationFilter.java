package com.bs.mycareer.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bs.mycareer.dto.BSUserDetail;
import com.bs.mycareer.service.BSUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;


public class UserAuthorizationFilter extends OncePerRequestFilter {

        private final JWTUtil jwtUtil;
        private final BSUserDetailsService bsUserDetailsService;


        public UserAuthorizationFilter(JWTUtil jwtUtil, BSUserDetailsService bsUserDetailsService){
            this.jwtUtil = jwtUtil;
            this.bsUserDetailsService = bsUserDetailsService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        FilterChain filterChain) throws ServletException, IOException {
            String header = httpServletRequest.getHeader("Authorization");

            if(StringUtils.hasText(header) || !jwtUtil.isStartWithPrefix(header)){
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            Optional<DecodedJWT> decodedJWT = jwtUtil.verifyAccessToken(header);
            if(!jwtUtil.isAccessToken(header) || decodedJWT.isEmpty()){
                throw new IllegalArgumentException("INVALID ACCESS TOKEN");
            }

            String email = decodedJWT.get().getClaim("email").asString();
            BSUserDetail bsUserDetail = (BSUserDetail) bsUserDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.authenticated(bsUserDetail, null, bsUserDetail.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);

            filterChain.doFilter(httpServletRequest, httpServletResponse);

            }
}

