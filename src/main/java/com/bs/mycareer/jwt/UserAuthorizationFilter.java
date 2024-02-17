package com.bs.mycareer.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bs.mycareer.User.dto.BSUserDetail;
import com.bs.mycareer.User.service.BSUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class UserAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private final JWTUtil jwtUtil;

    @Autowired
    private final BSUserDetailsService bsUserDetailsService;


    public UserAuthorizationFilter(JWTUtil jwtUtil, BSUserDetailsService bsUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.bsUserDetailsService = bsUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");

        if (token == null || !jwtUtil.isStartWithPrefix(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        if (token.contains("Bearer ")) {
            jwtUtil.removePrefix(token);
        }

        DecodedJWT decodedJWT = jwtUtil.verifyAccessToken(token);

        if (!jwtUtil.isAccessToken(token)) {
            throw new IllegalArgumentException("INVALID ACCESS TOKEN");
        }

        String email = decodedJWT.getClaim(token).asString();
        BSUserDetail bsUserDetail = (BSUserDetail) bsUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(bsUserDetail, null, bsUserDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

