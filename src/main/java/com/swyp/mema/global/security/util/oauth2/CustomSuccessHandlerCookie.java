package com.swyp.mema.global.security.util.oauth2;

import com.swyp.mema.domain.user.dto.CustomOAuthUser;
import com.swyp.mema.global.config.env.EnvConfig;
import com.swyp.mema.global.security.util.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandlerCookie extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final EnvConfig envConfig;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("custom success handler cookie");

        CustomOAuthUser customOAuthUser = (CustomOAuthUser) authentication.getPrincipal();
        String email = customOAuthUser.getEmail();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();
        String role = authority.getAuthority();

        String token = jwtUtil.createToken(email, role, 60 * 60 * 6000L);

        String redirect = "http://" + envConfig.getCilentIp() + ":3000";
        log.info("redirect : {}" + redirect);
        response.addCookie(createCookie("Authorization", token));
        response.sendRedirect(redirect);
    }

    private Cookie createCookie(String key, String value){

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}
