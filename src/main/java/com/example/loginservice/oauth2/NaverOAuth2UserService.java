package com.example.loginservice.oauth2;

import com.example.loginservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class NaverOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        Assert.notNull(userRequest, "userRequest cannot be null");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User user= delegate.loadUser(userRequest);

        ClientRegistration.ProviderDetails userInfoEndPoint = userRequest.getClientRegistration().getProviderDetails();
        userInfoEndPoint.getTokenUri();

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // Naver
        String userNameAttribute = userInfoEndPoint.getUserInfoEndpoint().getUserNameAttributeName(); // 로그인 진행 시 PK : 구글은 지원하지만 네이버와 카카오는 지원하지 않기 때문
    }
}
