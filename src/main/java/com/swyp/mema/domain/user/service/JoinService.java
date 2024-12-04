package com.swyp.mema.domain.user.service;

import com.swyp.mema.domain.user.dto.UserDTO;
import com.swyp.mema.domain.user.dto.converter.UserConverter;
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

    public boolean joinProcess(UserDTO userDTO) {
        System.out.println("join service - joinProcess");

        String email = userDTO.getEmail();
        String password = userDTO.getPassword();

        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {

            /*
                이 부분 exception 던지는 걸로 바꾸기
             */
            return false;
        }
        System.out.println("join service - joinProcess - enter");

        User user = UserConverter.convertUserDTO2User(userDTO, bCryptPasswordEncoder.encode(password));
        user.setRole("ROLE_CUSTOM");
        System.out.println("email = " + user.getEmail());
        System.out.println("password = " + user.getPassword());
        System.out.println("role = " + user.getRole());
        System.out.println("puz_color = " + user.getPuzColor());
        System.out.println("puz_id = " + user.getPuzId());
        userRepository.save(user);
        return true;
    }
}
