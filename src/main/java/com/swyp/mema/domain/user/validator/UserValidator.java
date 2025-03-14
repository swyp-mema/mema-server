package com.swyp.mema.domain.user.validator;

import com.swyp.mema.domain.user.exception.EmailAlreadyExistException;
import com.swyp.mema.domain.user.exception.UserNotFoundException;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.repository.UserRepository;
import com.swyp.mema.global.validation.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator implements EntityValidator<User, Long> {

    private final UserRepository userRepository;

    // 유저 존재 여부 검증 (userID 기반)
    @Override
    public User validateExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    // 이메일 중복 검증
    public void checkEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistException();
        }
    }

    // 이메일로 유저 찾기 (Optional 사용)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 이메일로 가입된 유저를 찾을 수 없습니다."));
    }
}
