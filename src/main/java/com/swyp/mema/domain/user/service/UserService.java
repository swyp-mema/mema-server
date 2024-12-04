package com.swyp.mema.domain.user.service;

import com.swyp.mema.domain.user.dto.CustomUserDetails;
import com.swyp.mema.domain.user.dto.converter.UserConverter;
import com.swyp.mema.domain.user.dto.request.PatchUserInfoReq;
import com.swyp.mema.domain.user.dto.response.UserInfoRes;
import com.swyp.mema.domain.user.exception.UserNotFoundException;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    // 사용자 조회 (예외 처리 포함)
    public User getUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    // 내 정보 상세보기
    public UserInfoRes getUserInfoDetail(CustomUserDetails userDetails) {

        User user = getUserById(userDetails.getUserId());
        return userConverter.user2UserInfoRes(user);
    }

    @Transactional
    public void patchUserInfo(PatchUserInfoReq req,CustomUserDetails userDetails) {

        User user = getUserById(userDetails.getUserId());
        updateFieldIfNotNull(req.getNickname(), user::setNickname);
        updateFieldIfNotNull(req.getPuzColor(), user::setPuzColor);
        updateFieldIfNotNull(req.getPuzId(), user::setPuzId);
    }

    // 유틸리티 메서드: 값이 null이 아닐 경우 필드 업데이트
    private <T> void updateFieldIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
