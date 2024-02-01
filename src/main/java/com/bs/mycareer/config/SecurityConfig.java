package com.bs.mycareer.config;

import com.bs.mycareer.jwt.JWTUtil;
import com.bs.mycareer.jwt.UserAuthenticationFilter;
import com.bs.mycareer.jwt.UserAuthorizationFilter;
import com.bs.mycareer.service.BSUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

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

    private final BSUserDetailsService bsUserDetailsService;


    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, BSUserDetailsService bsUserDetailsService) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.bsUserDetailsService = bsUserDetailsService;
    }

    //authenciationManager를 get을 해서 가져오면 여러 인증시스템에서 authenticationmanager를 생성이 아니라 호출해서 사용하면 확정성에 용이
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean // 스프링 컨테이너(Spring Container)에 의해 관리되는 메서드로 의미수동으로 해당 어노테이션을 통하여 주입을 합니다.
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserAuthenticationFilter userAuthenticationFilter(BCryptPasswordEncoder bCryptPasswordEncoder, BSUserDetailsService bsUserDetailsService, JWTUtil jwtUtil){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        authenticationProvider.setUserDetailsService(bsUserDetailsService);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter(providerManager, jwtUtil, bsUserDetailsService);
        userAuthenticationFilter.setAuthenticationManager(providerManager);
        userAuthenticationFilter.setFilterProcessesUrl("/user/token");
        userAuthenticationFilter.setPostOnly(true);
        return userAuthenticationFilter;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf disable -> csrf : 사용자 의지와 무관하게 공격자의 의도대로 서버에 특정 요청을 하도록 함
        //Form 로그인 방식 disable , http basic 인증 방식 disable
        http
                .csrf((auth) -> auth.disable())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable());

        // 1. UserAuthFilter는 2.UsernamePasswordAuthenticationfilter를 대신하는 UserAuthenticationFilter 이 필터 전에 있다 (위치)
        http
                .addFilterBefore(new UserAuthorizationFilter(jwtUtil,bsUserDetailsService), UserAuthenticationFilter.class)
                .addFilterAt(new UserAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil, bsUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        //경로별 인가 작업(내일 상세하게 보기)
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "*/*", "/register").permitAll()
                        .requestMatchers("/user").hasRole("USER")
                        .anyRequest().authenticated());
        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(httpServletRequest -> {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                })));

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}