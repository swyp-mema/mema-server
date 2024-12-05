package com.swyp.mema.domain.user.service;

import com.swyp.mema.domain.user.dto.request.UserReq;
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

    public boolean joinProcess(UserReq userReq) {
        System.out.println("join service - joinProcess");

        String email = userReq.getEmail();
        String password = userReq.getPassword();

        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {

            /*
                이 부분 exception 던지는 걸로 바꾸기
             */
            throw new EmailAlreadyExistException();
        }
        System.out.println("join service - joinProcess - enter");

        userReq.setRole("ROLE_CUSTOM");
        User user = UserConverter.convertUserDTO2User(userReq, bCryptPasswordEncoder.encode(password));
        System.out.println("email = " + user.getEmail());
        System.out.println("password = " + user.getPassword());
        System.out.println("role = " + user.getRole());
        System.out.println("puz_color = " + user.getPuzColor());
        System.out.println("puz_id = " + user.getPuzId());
        userRepository.save(user);
        return true;
    }
}
