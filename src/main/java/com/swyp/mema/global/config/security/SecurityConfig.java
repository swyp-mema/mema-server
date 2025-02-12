package com.swyp.mema.global.config.security;

import com.swyp.mema.global.security.filter.authentication.CustomAuthenticationEntryPoint;
import com.swyp.mema.global.security.filter.jwt.JWTFilter;
import com.swyp.mema.global.security.filter.jwt.JWTLoginFilter;
import com.swyp.mema.global.security.util.jwt.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;
	private final AuthenticationConfiguration authenticationConfiguration;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    // private final CustomOAuthUserService customOAuthUserService;
    // private final CustomSuccessHandlerCookie customSuccessHandlerCookie;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		log.info("=== filter chain start ===");

		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Spring Security에서 CORS 처리
			.csrf(csrf -> csrf.disable()) // CSRF 비활성화
			.formLogin(formLogin -> formLogin.disable())
			.httpBasic(httpBasic -> httpBasic.disable());

		//jwt 검증 필터 등록
		http
			.addFilterBefore(new JWTFilter(jwtUtil), JWTLoginFilter.class)
			.addFilterAt(new JWTLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
				UsernamePasswordAuthenticationFilter.class);

		// 예외 처리 핸들러
		http
			.exceptionHandling(exception -> exception
					.authenticationEntryPoint(authenticationEntryPoint) // 401 에러 핸들러
				//.accessDeniedHandler(accessDeniedHandler) // 403 에러 핸들러
			);

		// 세션 정책 설정
		http
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

		// 요청 인증 정책 설정
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/login", "/join/custom", "/midloc/test1", "/near/station","/midloc/test2/{stationId}/{upDown}","/midloc/test3","/DB/test1", "/DB/test2", "/DB/test3", "/DB/addNext/{line}/{curStationName}/{nextStationName}", "/DB/NextStation/{nextStationId}", "/login/naver", "/join/custom/sendEmail", "/join/custom/checkEmail").permitAll()

				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
				.anyRequest().authenticated()
			);

		//        http
		//                .sessionManagement((session) -> session
		//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

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

		return http.build();
	}

    // CORS 설정을 별도의 Bean으로 관리
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 도메인 (프론트엔드 도메인)
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "https://localhost:3000",
            "https://meet-mate-mema.vercel.app"
        ));

        // 허용할 HTTP 메서드 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 허용할 헤더 설정
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));

        // CORS 요청에서 쿠키 전송 허용
        configuration.setAllowCredentials(true);

        // 응답 헤더 노출 허용 (토큰 관련)
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authentication", "Authorization", "JSESSIONID"));

        // CORS 설정 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
