package com.swyp.mema.domain.user.dto.response.oauth2;

public interface OAuthResponse {

    String getProvider();

    String getEmail();
    //name == default nickname
    String getName();

    String getNickname();

    String getRole();
}
