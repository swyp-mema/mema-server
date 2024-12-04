package com.swyp.mema.domain.user.dto.oauth2;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class NaverResponse implements OAuthResponse {

    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {

        return "naver";
    }

    @Override
    public String getEmail() {

        Map<String, Object> response = (Map<String, Object>) attribute.get("response");
        String email = response !=null ? response.getOrDefault("id", "unknown-id").toString() : "unknown-id"
                + "@naver.com";
        return email;
    }

    @Override
    public String getName() {
        // "response" 객체에서 "name"을 가져옴
        Map<String, Object> response = (Map<String, Object>) attribute.get("response");
        return response != null ? response.getOrDefault("name", "Unknown").toString() : "Unknown";
    }

    @Override
    public String getNickname() {
        // "response" 객체에서 "nickname"을 가져옴
        Map<String, Object> response = (Map<String, Object>) attribute.get("response");
        return response != null ? response.getOrDefault("nickname", "Unknown").toString() : "Unknown";
    }

    @Override
    public String getRole() {

        return "ROLE_NAVER";
    }

}
