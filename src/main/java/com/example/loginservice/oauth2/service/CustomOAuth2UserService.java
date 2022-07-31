package com.example.loginservice.oauth2.service;

import com.example.loginservice.domain.AuthProvider;
import com.example.loginservice.domain.Role;
import com.example.loginservice.domain.User;
import com.example.loginservice.domain.dto.UserPrincipal;
import com.example.loginservice.oauth2.dto.NaverOauth2UserInfo;
import com.example.loginservice.oauth2.dto.OAuth2UserInfo;
import com.example.loginservice.oauth2.dto.OAuth2UserInfoFactory;
import com.example.loginservice.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.example.loginservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Optional;
/**
 * Spring OAuth2 에서 제공하는 OAuth2User 을 가공하여 OAuth2UserInfo 로 만들고
 * OAuth2UserInfo 에 Email 이 있는 지 검사와, A 라는 플랫폼으로 가입이 되어있는 데, B 플랫폼으로 가입 하려는 경우 검사를 진행
 * 이미 존재하는 계정에 경우에는 Update 를 진행하고,
 * 없는 경우에는 새로 Insert 하며, UserPrincipal 을 리턴한다
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("OAuth2 provider에 이메일이 없습니다.");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getProvider()
                    .equals(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toLowerCase()))) {
                throw new OAuth2AuthenticationProcessingException("이미 등록된 회원입니다.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(userRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        User user = User.builder()
                .email(oAuth2UserInfo.getEmail())
                .name(oAuth2UserInfo.getName())
                .nickname(oAuth2UserInfo.getNickname())
                .provider(AuthProvider.valueOf(provider.toLowerCase()))
                .providerId(oAuth2UserInfo.getId())
                .role(Role.CONSUMER)
                .build();

        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setNickname(oAuth2UserInfo.getNickname());
        return userRepository.save(existingUser);
    }
}
