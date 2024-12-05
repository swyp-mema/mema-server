package com.swyp.mema.domain.user.controller;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String mainP(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return "main Controller" + " " + name;
    }

    @GetMapping("/mypage")
    public SecurityContext mypage() {
        System.out.println(SecurityContextHolder.getContext());
        return SecurityContextHolder.getContext();
//        return "mypage";
    }

    @GetMapping("/login/naver")
    public String naver_login(){
        return "naver";
    }
}
