package com.example.loginservice.oauth2;

public interface CustomOauth2User {

    String oauth2Id();
    String getEmail();
    String getName();
    String getNameAttributeKey();
}
