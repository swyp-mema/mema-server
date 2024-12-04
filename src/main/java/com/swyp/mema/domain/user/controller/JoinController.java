package com.swyp.mema.domain.user.controller;

import com.swyp.mema.domain.user.dto.UserDTO;
import com.swyp.mema.domain.user.service.JoinService;
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
    public ResponseEntity<String> joinCustom(@RequestBody UserDTO userDTO) {

        if (!joinService.joinProcess(userDTO)) {
            return ResponseEntity.badRequest().body("email exist");
        }

        return ResponseEntity.ok("OK");
    }

}
