package com.example.loginservice.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverOauthDto {

    private NaverUserProfileResponse response;

    @Getter
    @NoArgsConstructor
    public static class NaverUserProfileResponse {

        private String id;
        private String nickname;
        private String name;
        private String email;
        private String gender;
        private String age;
        private String birthday;
        private String birthyear;
        private String mobile;
    }
}
