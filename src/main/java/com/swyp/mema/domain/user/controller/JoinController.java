package com.swyp.mema.domain.user.controller;

import com.swyp.mema.domain.user.dto.request.UserReq;
import com.swyp.mema.domain.user.service.JoinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join/custom")
    public ResponseEntity<String> joinCustom(@Valid @RequestBody UserReq userReq) {

        if (!joinService.joinProcess(userReq)) {
            return ResponseEntity.badRequest().body("email exist");
        }

        return ResponseEntity.ok("OK");
    }

}
