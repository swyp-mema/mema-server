package com.swyp.mema.domain.user.dto.oauth2;

import com.swyp.mema.domain.user.dto.request.UserReq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuthUser implements OAuth2User {

    private final UserReq userReq;

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

                return userReq.getRole();
            }
        });

        return collection;

    }

    @Override
    public String getName() {
        return userReq.getUsername();
    }

    public String getEmail(){
        return userReq.getEmail();
    }

    public String getNickname() {
        return userReq.getNickname();
    }

}
