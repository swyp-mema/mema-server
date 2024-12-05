package com.swyp.mema.domain.user.converter;

import com.swyp.mema.domain.user.dto.request.UserReq;
import com.swyp.mema.domain.user.model.User;

public class UserConverter {

    public static User convertUserDTO2User(UserReq userReq) {

        User user = User.builder()
                .email(userReq.getEmail())
                .nickname(userReq.getNickname())
                .puzId(userReq.getPuz_id())
                .puzColor(userReq.getPuz_color())
                .role(userReq.getRole())
                .build();
        user.setUsername(userReq.getUsername());
        return user;
    }

    public static User convertUserDTO2User(UserReq userReq, String password) {

        User user = User.builder()
                .email(userReq.getEmail())
                .password(password)
                .nickname(userReq.getNickname())
                .puzId(userReq.getPuz_id())
                .puzColor(userReq.getPuz_color())
                .role(userReq.getRole())
                .build();
        user.setUsername(userReq.getUsername());
        return user;
    }


    public static UserReq convertUserEntity2UserDTO(User user) {


        UserReq userReq = new UserReq();
        userReq.setUsername(user.getUsername());
        userReq.setEmail(user.getEmail());
        userReq.setNickname(user.getNickname());
        userReq.setRole(user.getRole());
        userReq.setPuz_id(user.getPuzId());
        userReq.setPuz_color(user.getPuzColor());
        return userReq;
    }
}
