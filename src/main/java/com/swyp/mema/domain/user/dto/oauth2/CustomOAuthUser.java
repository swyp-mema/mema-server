package com.swyp.mema.domain.user.dto.oauth2;

import com.swyp.mema.domain.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuthUser implements OAuth2User {

    private final UserDTO userDTO;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                return userDTO.getRole();
            }
        });

        return collection;

    }

    @Override
    public String getName() {
        return userDTO.getUsername();
    }

    public String getEmail(){
        return userDTO.getEmail();
    }

    public String getNickname() {
        return userDTO.getNickname();
    }

}
