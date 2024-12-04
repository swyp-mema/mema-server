package com.swyp.mema.domain.user.dto.converter;

import com.swyp.mema.domain.user.dto.UserDTO;
import com.swyp.mema.domain.user.entity.UserEntity;

public class UserConverter {

    public static UserEntity convertUserDTO2User(UserDTO userDTO) {

        UserEntity userEntity = UserEntity.builder()
                .email(userDTO.getEmail())
                .nickname(userDTO.getNickname())
                .puz_id(userDTO.getPuz_id())
                .puz_color(userDTO.getPuz_color())
                .role(userDTO.getRole())
                .build();
        userEntity.setUsername(userDTO.getUsername());
        return userEntity;
    }

    public static UserEntity convertUserDTO2User(UserDTO userDTO, String password) {

        UserEntity userEntity = UserEntity.builder()
                .email(userDTO.getEmail())
                .password(password)
                .nickname(userDTO.getNickname())
                .puz_id(userDTO.getPuz_id())
                .puz_color(userDTO.getPuz_color())
                .role(userDTO.getRole())
                .build();
        userEntity.setUsername(userDTO.getUsername());
        return userEntity;
    }


    public static UserDTO convertUserEntity2UserDTO(UserEntity userEntity) {


        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setNickname(userEntity.getNickname());
        userDTO.setRole(userEntity.getRole());
        userDTO.setPuz_id(userEntity.getPuz_id());
        userDTO.setPuz_color(userEntity.getPuz_color());
        return userDTO;
    }
}
