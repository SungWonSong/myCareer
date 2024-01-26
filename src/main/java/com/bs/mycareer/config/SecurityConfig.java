package com.bs.mycareer.config;

import com.bs.mycareer.jwt.JWTUtil;
import com.bs.mycareer.jwt.UserAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // // Spring Security의 기본 보안 설정이 적용, 기본적인 로그인, 로그아웃, CSRF(Cross-Site Request Forgery) 방어 등이 활성화됩니다.
// (WebSecurityConfigurerAdapter를 상속받아 사용)
@EnableMethodSecurity(securedEnabled = true)
// - 어노테이션의 securedEnabled 속성은 @Secured 어노테이션을 사용하여 메소드 레벨의 보안을 활성화할지 여부를 나타냅니다
// - @Secured 어노테이션을 사용하여 메소드에 직접 보안 제어를 할 수 있습니다.(메소드의 보안 제어)


public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean // 스프링 컨테이너(Spring Container)에 의해 관리되는 메서드로 의미수동으로 해당 어노테이션을 통하여 주입을 합니다.
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable -> csrf : 사용자 의지와 무관하게 공격자의 의도대로 서버에 특정 요청을 하도록 함
        http
                .csrf((auth) -> auth.disable());

        //Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());
        //필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        http
                .addFilterAt(new UserAuthenticationFilter(authenticationManagerBean(), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //경로별 인가 작업(내일 상세하게 보기)
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join").permitAll()
                      //  .requestMatchers("/user").hasRole("user")
                        .anyRequest().authenticated());

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}