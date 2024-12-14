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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilterOAuth extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        System.out.println("jwtfilter OAuth - do internal filter");
        System.out.println(requestUri);
        System.out.println(request);

        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/join(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("jwtfilter OAuth - do internal filter - enter");
//        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
//
//            filterChain.doFilter(request, response);
//            return;
//        }
        System.out.println("jwt filter cookie");
        String authorization = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {

            System.out.println("Cookie empty");
            filterChain.doFilter(request, response);
            return;
        }
        for (Cookie cookie : cookies) {

            System.out.println("Cookie get");
            System.out.println(cookie.getName() + " : " + cookie.getValue());
            if (cookie.getName().equals("Authorization")) {

                authorization = cookie.getValue();
                System.out.println("authorization: " + authorization);
            }
        }

        if (authorization == null) {

            System.out.println("Cookie authorization empty");
            filterChain.doFilter(request, response);

            return;
        }

        String token = authorization;

        if (jwtUtil.isExpired(token)) {

            System.out.println("cookie - token expired");
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
