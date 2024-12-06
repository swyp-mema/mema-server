package com.swyp.mema.domain.user.controller;

import com.swyp.mema.domain.user.dto.CustomUserDetails;
import com.swyp.mema.domain.user.dto.request.UpdateUserInfoReq;
import com.swyp.mema.domain.user.dto.response.UserInfoRes;
import com.swyp.mema.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public String mainP(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return "main Controller" + " " + name;
    }

    @GetMapping("/mypage/test")
    public SecurityContext mypage() {
        System.out.println(SecurityContextHolder.getContext());
        return SecurityContextHolder.getContext();
    }

    @GetMapping("/login/naver")
    public String naver_login(){
        return "naver";
    }

    @Operation(summary = "내 정보 조회 API", description = "회원 상세정보를 조회합니다.")
    @GetMapping("/mypage")
    public ResponseEntity<UserInfoRes> myInfo(@AuthenticationPrincipal CustomUserDetails userDetails){

        UserInfoRes response = userService.getUserInfoDetail(userDetails);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 정보 수정 API", description = "회원 상세정보를 수정합니다.")
    @PatchMapping("/mypage")
    public ResponseEntity<UserInfoRes> UpdateMyInfo(@RequestBody UpdateUserInfoReq req, @AuthenticationPrincipal CustomUserDetails userDetails){

        UserInfoRes userInfoRes = userService.updateUserInfo(req,userDetails);
        return ResponseEntity.ok(userInfoRes);
    }
}
