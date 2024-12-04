package com.swyp.mema.domain.user.dto.converter;

import com.swyp.mema.domain.user.dto.UserDTO;
import com.swyp.mema.domain.user.dto.response.UserRes;
import com.swyp.mema.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    public User userDto2User(UserDTO userDTO) {

        User user = User.builder()
                .email(userDTO.getEmail())
                .nickname(userDTO.getNickname())
                .puzId(userDTO.getPuz_id())
                .puzColor(userDTO.getPuz_color())
                .role(userDTO.getRole())
                .build();
        user.setUsername(userDTO.getUsername());
        return user;
    }

    public User userDto2User(UserDTO userDTO, String password) {

        User user = User.builder()
                .email(userDTO.getEmail())
                .password(password)
                .nickname(userDTO.getNickname())
                .puzId(userDTO.getPuz_id())
                .puzColor(userDTO.getPuz_color())
                .role(userDTO.getRole())
                .build();
        user.setUsername(userDTO.getUsername());
        return user;
    }


    public UserDTO userEntity2UserDto(User user) {


        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setNickname(user.getNickname());
        userDTO.setRole(user.getRole());
        userDTO.setPuz_id(user.getPuzId());
        userDTO.setPuz_color(user.getPuzColor());
        return userDTO;
    }
}
