package com.swyp.mema.domain.user.service;

import com.swyp.mema.domain.badge.model.Badge;
import com.swyp.mema.domain.badge.repository.BadgeRepository;
import com.swyp.mema.domain.user.dto.request.JoinReq;
import com.swyp.mema.domain.user.converter.UserConverter;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.repository.UserRepository;
import com.swyp.mema.global.validation.ValidationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final ValidationFacade validationFacade;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final BadgeRepository badgeRepository;


    public boolean joinProcess(JoinReq joinReq) {

        String email = joinReq.getEmail();
        String password = joinReq.getPassword();

        checkEmail(email);

        User user = UserConverter.convertJoinReqToUser(joinReq, bCryptPasswordEncoder.encode(password), "ROLE_CUSTOM");
        userRepository.save(user);

        Badge badge = createBadge(user);
        badgeRepository.save(badge);
        return true;
    }

    // 이미 가입된 이메일인지 Facade 호출하여 검증
    public void checkEmail(String email) {
        validationFacade.checkEmailNotExists(email); // Facade 호출
    }

    private Badge createBadge(User user) {
        return Badge.builder()
                .user(user)
                .build();
    }
}
