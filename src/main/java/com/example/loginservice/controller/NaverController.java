package com.example.loginservice.controller;

import com.example.loginservice.controller.dto.NaverOauthDto;
import com.example.loginservice.domain.user.Role;
import com.example.loginservice.domain.user.User;
import com.example.loginservice.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/login/oauth2/code")
@RequiredArgsConstructor
public class NaverController {

    /**
     * https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=HS9ffEi_MQWPEq5MWvmL&state=skdfeidjfslslsl&redirect_uri=http://localhost:8080/login/oauth2/code/naver
     */
    private final UserRepository userRepository;

    @GetMapping("/naver")
    @ResponseBody
    public User naverOauthRedirect(@RequestParam String code, @RequestParam String state) {

        /**
         * Client요청으로부터 시작
         * AccessToken 발급 받기
         */

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders accessTokenHeader = new HttpHeaders();
        accessTokenHeader.add("Content-Type", "application/X-www-form-urlencoded");

        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("grant_type", "authorization_code");
        tokenRequest.add("client_id", "HS9ffEi_MQWPEq5MWvmL"); // 숨겨야됨

        tokenRequest.add("code", code);
        tokenRequest.add("state", state);

        HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(tokenRequest, accessTokenHeader);

        ResponseEntity<String> accessTokenResponse = restTemplate.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                accessTokenRequest,
                String.class
        );

        /**
         * accessToken을 이용해서 userProfile을 받아온다
         */

        HttpHeaders userProfileApiHeader = new HttpHeaders();
        userProfileApiHeader.add("Authorization", "Bearer " + accessTokenResponse.getBody());

        HttpEntity<HttpHeaders> userProfileApiEntity = new HttpEntity<>(userProfileApiHeader);

        ResponseEntity<String> userProfileApiResponse = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                userProfileApiEntity,
                String.class
        );

        /**
         * AccessToken 발급 완료
         * 1. DB에 사용자가 있는지 확인
         * 2. 만약 없다면 데이터베이스에 삽입
         * 3. 만약 있다면 사용자 정보를 데이터베이스에서 가져옴
         */

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        NaverOauthDto responseDto = null;
        try {
            responseDto = objectMapper.readValue(userProfileApiResponse.getBody(), NaverOauthDto.class);
        } catch (JsonProcessingException e) {
            /**
             * 커스텀 예외 필요
             */
            throw new RuntimeException(e);
        }

        final NaverOauthDto.NaverUserProfileResponse userProfile = responseDto.getResponse();
        final Role authority;
//        if(role.equals("Seller")) {
//            authority = Role.SELLER;
//        } else {
//            authority = Role.CUSTOMER;
//        }

        User findUser = userRepository.findByOauth2Id(responseDto.getResponse().getId()).orElseGet(() ->
                userRepository.save(
                        User.of(
                                userProfile.getName(),
                                userProfile.getEmail(),
                                userProfile.getGender(),
                                userProfile.getAge(),
                                userProfile.getBirthday(),
                                userProfile.getBirthyear(),
                                userProfile.getMobile(),
                                Role.CUSTOMER))
        );

        return findUser;
    }
}
