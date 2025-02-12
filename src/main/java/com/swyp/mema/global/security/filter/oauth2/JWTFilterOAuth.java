package com.swyp.mema.global.security.filter.oauth2;

import com.swyp.mema.domain.user.dto.request.UserReq;
import com.swyp.mema.domain.user.dto.CustomOAuthUser;
import com.swyp.mema.global.security.util.jwt.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilterOAuth extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        log.info(requestUri);
        log.info("request : {}", request);

        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (requestUri.matches("^\\/join(?:\\/.*)?$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }
        for (Cookie cookie : cookies) {
            log.info(cookie.getName() + " : " + cookie.getValue());
            if (cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
                log.info("authorization : {}", authorization);
            }
        }

        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization;

        if (jwtUtil.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        UserReq userReq = new UserReq();
        userReq.setUsername(username);
        userReq.setRole(role);

        CustomOAuthUser customOAuthUser = new CustomOAuthUser(userReq);

        Authentication authToken = new UsernamePasswordAuthenticationToken(userReq, null, customOAuthUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
