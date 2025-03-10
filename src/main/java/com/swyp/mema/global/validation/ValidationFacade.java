package com.swyp.mema.global.validation;

import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationFacade {

    private final UserValidator userValidator;

    // userId 기반 유저 존재 여부 검증
    public User validateUserExists(Long userId) {
        return userValidator.validateExists(userId);
    }

    //  이메일 중복 검증
    public void checkEmailNotExists(String email) {
        userValidator.checkEmailNotExists(email);
    }

    // 이메일을 통한 유저 조회
    public User findByEmail(String email) {
        return userValidator.findByEmail(email);
    }
}
