package com.swyp.mema.domain.user.service;

import com.swyp.mema.domain.user.dto.CustomUserDetails;
import com.swyp.mema.domain.user.dto.converter.UserConverter;
import com.swyp.mema.domain.user.dto.request.UpdatePasswordReq;
import com.swyp.mema.domain.user.dto.request.UpdateUserInfoReq;
import com.swyp.mema.domain.user.dto.response.UserInfoRes;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.repository.UserRepository;
import com.swyp.mema.global.validation.ValidationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ValidationFacade validationFacade;
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 사용자 조회 (예외 처리 포함)
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return validationFacade.validateUserExists(userId);
    }

    // 내 정보 상세보기
    @Transactional(readOnly = true)
    public UserInfoRes getUserInfoDetail(CustomUserDetails userDetails) {

        User user = getUserById(userDetails.getUserId());
        return userConverter.user2UserInfoRes(user);
    }

    // 유저정보 수정
    @Transactional
    public UserInfoRes updateUserInfo(UpdateUserInfoReq req, CustomUserDetails userDetails) {

        User user = getUserById(userDetails.getUserId());
        updateFieldIfNotNull(req.getNickname(), user::setNickname);
        updateFieldIfNotNull(req.getPuzzleColor(), user::setPuzColor);
        updateFieldIfNotNull(req.getPuzzleId(), user::setPuzId);
        return userConverter.user2UserInfoRes(user);
    }

    @Transactional
    public void updateUserPassword(UpdatePasswordReq req, CustomUserDetails userDetails) {

        User user = getUserById(userDetails.getUserId());
        user.updatePassword(bCryptPasswordEncoder.encode(req.getPassword()));
    }

    @Transactional
    public void deleteUser(){

        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        validationFacade.validateUserExists(userId);    // Facade 통해 검증 후 삭제 진행
        userRepository.deleteById(userId);
    }

    // 유틸리티 메서드: 값이 null이 아닐 경우 필드 업데이트
    private <T> void updateFieldIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
