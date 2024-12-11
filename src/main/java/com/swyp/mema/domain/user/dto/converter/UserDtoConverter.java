package com.swyp.mema.domain.user.dto.converter;

import com.swyp.mema.domain.user.dto.request.UserReq;
import com.swyp.mema.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public User userDto2User(UserReq userReq) {

        User user = User.builder()
                .email(userReq.getEmail())
                .nickname(userReq.getNickname())
                .puzId(userReq.getPuzzleId())
                .puzColor(userReq.getPuzzleColor())
                .role(userReq.getRole())
                .build();
        user.setUsername(userReq.getUsername());
        return user;
    }

    public User userDto2User(UserReq userReq, String password) {

        User user = User.builder()
                .email(userReq.getEmail())
                .password(password)
                .nickname(userReq.getNickname())
                .puzId(userReq.getPuzzleId())
                .puzColor(userReq.getPuzzleColor())
                .role(userReq.getRole())
                .build();
        user.setUsername(userReq.getUsername());
        return user;
    }


    public UserReq userEntity2UserDto(User user) {


        UserReq userReq = new UserReq();
        userReq.setUsername(user.getUsername());
        userReq.setEmail(user.getEmail());
        userReq.setNickname(user.getNickname());
        userReq.setRole(user.getRole());
        userReq.setPuzzleId(user.getPuzId());
        userReq.setPuzzleColor(user.getPuzColor());
        return userReq;
    }
}
