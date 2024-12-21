package com.swyp.mema.domain.user.controller;

import com.swyp.mema.domain.user.dto.request.EmailCheckReq;
import com.swyp.mema.domain.user.dto.request.EmailReq;
import com.swyp.mema.domain.user.dto.request.JoinReq;
import com.swyp.mema.domain.user.dto.response.EmailRes;
import com.swyp.mema.domain.user.service.EmailAuthServiceCustom;
import com.swyp.mema.domain.user.service.JoinService;

import com.swyp.mema.domain.user.service.EmailAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "사용자", description = "사용자 관련 API")
public class JoinController {

    private final JoinService joinService;
    private final EmailAuthServiceCustom emailAuthService;

    @Operation(summary = "이메일 회원가입", description = "사용자는 이메일과 비밀번호를 가지고 회원가입을 할 수 있다.",
        tags = "사용자") // 보안 요구사항 제거)
    @PostMapping("/join/custom")
    public ResponseEntity<String> joinCustom(@Valid @RequestBody JoinReq joinReq) {

        if (!joinService.joinProcess(joinReq)) {
            return ResponseEntity.badRequest().body("email exist");
        }
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/join/custom/sendEmail")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailReq emailReq, HttpServletRequest request, HttpServletResponse response) {

        String email = emailReq.getEmail();
        joinService.checkEmail(email);
        emailAuthService.sendMail(email, request, response);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/join/custom/checkEmail")
    public ResponseEntity<String> checkEmail(@Valid @RequestBody EmailCheckReq emailCheckReq, HttpServletRequest request) {

        emailAuthService.checkCode(emailCheckReq, request);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/join/custom/test")
    public ResponseEntity<String> testEmail(@Valid @RequestBody EmailReq emailReq, HttpServletRequest request, HttpServletResponse response) {

        String email = emailReq.getEmail();
        emailAuthService.sendMail(email, request, response);
        return ResponseEntity.ok("OK");
    }

}
