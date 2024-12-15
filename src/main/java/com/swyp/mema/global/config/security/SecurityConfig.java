package com.swyp.mema.global.config.security;

import com.swyp.mema.domain.user.service.CustomOAuthUserService;
import com.swyp.mema.global.security.filter.authentication.CustomAuthenticationEntryPoint;
import com.swyp.mema.global.security.filter.jwt.JWTFilter;
import com.swyp.mema.global.security.filter.oauth2.JWTFilterOAuth;
import com.swyp.mema.global.security.filter.jwt.JWTLoginFilter;
import com.swyp.mema.global.security.util.oauth2.CustomSuccessHandlerCookie;
import com.swyp.mema.global.security.util.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final CustomOAuthUserService customOAuthUserService;
    private final CustomSuccessHandlerCookie customSuccessHandlerCookie;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
        System.out.println("filter chain");

        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Arrays.asList("http://meet-mate.duckdns.org/", "http://localhost:3000"));  // 개발 서버와 로컬 프론트엔드 도메인 추가
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);
                        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authentication"));
                        return configuration;
                    }
                })));

        http
                .csrf((auth) -> auth.disable());

        http
                .formLogin((auth) -> auth.disable());

        http
                .httpBasic((auth) -> auth.disable());

        //JWTFilterCookie(소셜 로그인 사용자용) 추가
//        http
//                .addFilterAfter(new JWTFilterOAuth(jwtUtil), OAuth2LoginAuthenticationFilter.class);
//
//        http
//                .oauth2Login((oauth2) -> oauth2
//                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
//                                .userService(customOAuthUserService))
//                                .successHandler(customSuccessHandlerCookie)
//                        );

        //jwt 검증 필터 등록
        http
                .addFilterBefore(new JWTFilter(jwtUtil), JWTLoginFilter.class);

        http
                .addFilterAt(new JWTLoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/join/custom", "/login/naver").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated());

        http
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint) // 401 에러 핸들러
//                        .accessDeniedHandler(accessDeniedHandler) // 403 에러 핸들러
                );

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
