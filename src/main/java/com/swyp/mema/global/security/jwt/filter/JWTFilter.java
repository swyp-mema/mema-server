package com.swyp.mema.global.security.jwt.filter;

import com.swyp.mema.domain.user.dto.CustomUserDetails;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.global.security.jwt.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest  request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("jwtfilter - do filter internal");
        String requestUri = request.getRequestURI();
        System.out.println(requestUri);

        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("jwtfilter - do filter internal - enter");
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {

            System.out.println("token null");
            filterChain.doFilter(request, response);

            return;
        }

        System.out.println("authorization now");

        String token = authorizationHeader.split(" ")[1];

        if (jwtUtil.isExpired(token)) {

            System.out.println("token is expired");
            filterChain.doFilter(request, response);

            return;
        }

        String userUsername = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        User user = new User();
        user.setUsername(userUsername);
        user.setPassword("tmp");
        user.setRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        System.out.println("end");
        filterChain.doFilter(request, response);
    }
}
