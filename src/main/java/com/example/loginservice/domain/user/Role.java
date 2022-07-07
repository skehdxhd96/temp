package com.example.loginservice.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    SELLER("ROLE_SELLER", "판매자") ,
    CUSTOMER("ROLE_CUSTOMER", "구매자");

    private final String key;
    private final String title;
}
