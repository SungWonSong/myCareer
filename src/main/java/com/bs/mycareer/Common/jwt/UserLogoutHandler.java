package com.bs.mycareer.Common.jwt;


import com.bs.mycareer.utils.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class UserLogoutHandler implements LogoutHandler, LogoutSuccessHandler {

    private final JWTUtil jwtUtil;

    public UserLogoutHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

        try {
            jwtUtil.destroyAccessToken(httpServletRequest,httpServletResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        JsonUtil.writeValue(response.getOutputStream(), "successfully logout!!!");
    }
}
