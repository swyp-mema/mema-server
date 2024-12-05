package com.swyp.mema.domain.user.service;

import com.swyp.mema.domain.user.dto.request.UserReq;
import com.swyp.mema.domain.user.dto.converter.UserConverter;
import com.swyp.mema.domain.user.dto.oauth2.CustomOAuthUser;
import com.swyp.mema.domain.user.dto.oauth2.NaverResponse;
import com.swyp.mema.domain.user.dto.oauth2.OAuthResponse;
import com.swyp.mema.domain.user.model.User;
import com.swyp.mema.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuthUserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("OAuth2 user service - load user");

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuthResponse 처리
        OAuthResponse oAuthResponse = getOAuthResponse(registrationId, oAuth2User);
        if (oAuthResponse == null) {
            throw new OAuth2AuthenticationException("지원하지 않는 OAuth2 Provider: " + registrationId);
        }

        // 이메일로 사용자 검색
        User user = userRepository.findByEmail(oAuthResponse.getEmail());
        if(user == null) {
            user = createNewUser(oAuthResponse);
            System.out.println(user.getUserId());
            System.out.println(user.getUsername());
            System.out.println(user.getEmail());
        }

        // UserDTO로 변환 및 반환
        UserReq userReq = UserConverter.convertUserEntity2UserDTO(user);
        return new CustomOAuthUser(userReq);
    }

    private OAuthResponse getOAuthResponse(String registrationId, OAuth2User oAuth2User) {
        switch (registrationId) {
            case "naver":
                System.out.println("naver login");
                return new NaverResponse(oAuth2User.getAttributes());
            default:
                return null;
        }
    }

    private User createNewUser(OAuthResponse oAuthResponse) {
        System.out.println("Creating new user entity");

        return userRepository.save(User.builder()
                .email(oAuthResponse.getEmail())
                .nickname(oAuthResponse.getNickname())
                .role(oAuthResponse.getRole())
                .puzId("default_puz_id") // 기본 값 설정
                .puzColor("default_puz_color") // 기본 값 설정
                .password(generateRandomPassword()) // 비밀번호 생성
                .build());
    }

    private String generateRandomPassword() {
        // UUID 기반 고유 문자열 생성
        return java.util.UUID.randomUUID().toString();
    }
}


