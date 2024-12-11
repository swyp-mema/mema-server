package com.swyp.mema.domain.user.converter;

import com.swyp.mema.domain.user.dto.request.JoinReq;
import com.swyp.mema.domain.user.dto.request.UserReq;
import com.swyp.mema.domain.user.model.User;

public class UserConverter {

    public static User convertUserDTO2User(UserReq userReq) {

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

    public static User convertUserDTO2User(UserReq userReq, String password) {

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

    public static User convertJoinReq2User(JoinReq joinReq, String password, String role) {

        User user = User.builder()
                .email(joinReq.getEmail())
                .password(password)
                .nickname(joinReq.getNickname())
                .role(role)
                .build();
        return user;
    }


    public static UserReq convertUserEntity2UserDTO(User user) {


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
