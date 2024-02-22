//package com.bs.mycareer.jwt;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.Collection;
//
//
//    public class CustomToken implements Authentication {
//        private String token;
//        private boolean authenticated;
//
//        public CustomToken(String token) {
//            this.token = token;
//            this.authenticated = false;
//        }
//
//        @Override
//        public Collection<? extends GrantedAuthority> getAuthorities() {
//            // 토큰에서 권한 정보를 추출하거나, 권한 정보를 관리하는 서비스를 통해 권한 정보를 가져옵니다.
//            return null;
//        }
//
//        @Override
//        public Object getCredentials() {
//            return token;
//        }
//
//        @Override
//        public Object getDetails() {
//            // 필요한 경우, 토큰에서 추가적인 정보를 추출하여 반환합니다.
//            return null;
//        }
//
//        @Override
//        public Object getPrincipal() {
//            // 토큰에서 사용자 식별 정보를 추출하여 반환합니다.
//            return null;
//        }
//
//        @Override
//        public boolean isAuthenticated() {
//            return authenticated;
//        }
//
//        @Override
//        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//            this.authenticated = isAuthenticated;
//        }
//
//        @Override
//        public String getName() {
//            // 토큰에서 사용자 이름을 추출하여 반환합니다.
//            return null;
//        }
//    }
//
