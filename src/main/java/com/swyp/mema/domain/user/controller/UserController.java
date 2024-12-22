package com.swyp.mema.domain.user.controller;

import com.swyp.mema.domain.user.dto.CustomUserDetails;
import com.swyp.mema.domain.user.dto.request.*;
import com.swyp.mema.domain.user.dto.response.UserInfoRes;
import com.swyp.mema.domain.user.exception.EmailNotMineException;
import com.swyp.mema.domain.user.repository.UserRepository;
import com.swyp.mema.domain.user.service.EmailAuthServiceCustom;
import com.swyp.mema.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Tag(name = "사용자", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;
    private final EmailAuthServiceCustom emailAuthService;
    private final UserRepository userRepository;

    @Operation(summary = "로그인 API", description = "커스텀유저의 로그인을 진행 합니다.")
    @PostMapping("/login")
    public void loginCustom(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                            @RequestBody LoginReq loginReq, HttpServletRequest request) {

        request.getHeader("Origin");
        return ;
    }

    @Operation(summary = "로그인 API", description = "커스텀유저의 로그인을 진행 합니다.")
    @PostMapping("/login/naver")
    public void loginNaver(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return;
    }

    @Operation(summary = "내 정보 조회 API", description = "회원 상세정보를 조회합니다.",
            tags = "사용자", security = {})
    @GetMapping("/mypage")
    public ResponseEntity<UserInfoRes> myInfo(@AuthenticationPrincipal CustomUserDetails userDetails){

        UserInfoRes response = userService.getUserInfoDetail(userDetails);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 정보 수정 API", description = "회원 상세정보를 수정합니다.",
        tags = "사용자", security = {})
    @PatchMapping("/mypage")
    public ResponseEntity<UserInfoRes> UpdateMyInfo(@RequestBody UpdateUserInfoReq req,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails){

        UserInfoRes userInfoRes = userService.updateUserInfo(req,userDetails);
        return ResponseEntity.ok(userInfoRes);
    }

    @Operation(summary = "내 비밀번호 수정 API", description = "회원 비밀번호를 수정합니다.",
        tags = "사용자", security = {})
    @PatchMapping("/mypage/auth")
    public ResponseEntity<String> UpdateMyPassword(@RequestBody UpdatePasswordReq req,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails){

        userService.updateUserPassword(req, userDetails);
        return ResponseEntity.ok("OK");
    }

    @Operation(summary = "이메일 인증 코드 발송 API", description = "비밀번호 변경 시 이메일 인증 코드를 발송합니다.",
            tags = "사용자", security = {})
    @PostMapping("/mypage/sendEmail")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailReq emailReq,
                                            @AuthenticationPrincipal CustomUserDetails userDetails,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {

        String email = emailReq.getEmail();
        if(!Objects.equals(userDetails.getUser().getEmail(), email)){
            throw new EmailNotMineException();
        }
        emailAuthService.sendMail(email, request, response);
        return ResponseEntity.

                ok("OK");
    }

    @Operation(summary = "이메일 인증 코드 검증 API", description = "비밀번호 변경 시 이메일 인증 코드를 검증합니다.",
            tags = "사용자", security = {})
    @PostMapping("/mypage/checkEmail")
    public ResponseEntity<String> checkEmail(@Valid @RequestBody EmailCheckReq emailCheckReq, HttpServletRequest request) {

        emailAuthService.checkCode(emailCheckReq, request);
        return ResponseEntity.ok("OK");
    }

    @Operation(summary = "회원 탈퇴", description = "사용자의 회원 정보를 삭제합니다.",
            tags = "사용자", security = {})
    @DeleteMapping("/mypage/resign")
    public ResponseEntity<String> resign(){

        userService.deleteUser();
        return ResponseEntity.ok("OK");
    }
}
