package com.swyp.mema.domain.user.service;

import com.swyp.mema.domain.badge.model.Badge;
import com.swyp.mema.domain.badge.repository.BadgeRepository;
import com.swyp.mema.domain.user.dto.request.JoinReq;
import com.swyp.mema.domain.user.converter.UserConverter;
import com.swyp.mema.domain.user.exception.EmailAlreadyExistException;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final BadgeRepository badgeRepository;

    public void checkEmail(String email) {

        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistException();
        }
    }

    public boolean joinProcess(JoinReq joinReq) {
        System.out.println("join service - joinProcess");

        String email = joinReq.getEmail();
        String password = joinReq.getPassword();

        checkEmail(email);
        System.out.println("join service - joinProcess - enter");

        User user = UserConverter.convertJoinReqToUser(joinReq, bCryptPasswordEncoder.encode(password), "ROLE_CUSTOM");
        System.out.println("email = " + user.getEmail());
        System.out.println("password = " + user.getPassword());
        System.out.println("role = " + user.getRole());
        System.out.println("puzzleColor = " + user.getPuzColor());
        System.out.println("puzzleId = " + user.getPuzId());
        userRepository.save(user);
        badgeRepository.save(Badge.builder()
                .user(user)
                .build());
        return true;
    }
}
