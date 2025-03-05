package com.swyp.mema.domain.midlocation.controller;

import com.swyp.mema.domain.midlocation.service.MidLocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class midLocationController {

    private final MidLocationService midLocationService;

    public midLocationController(MidLocationService midLocationService) {
        this.midLocationService = midLocationService;
    }

    @GetMapping("/midLocation/test1")
    public ResponseEntity<String> DBTest1() {

        return ResponseEntity.ok("");
    }
}
