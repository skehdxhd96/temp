package com.example.loginservice.config.provider;

import com.example.loginservice.config.token.CustomAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    /**
     * AuthenticationManager의 구현체가 ProviderManager
     * AuthenticationManager로부터 Authentication 객체를 전달받는다.
     * Authentication 구조가 맞다면 추가정보를 꺼낸다.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {

        /**
         * 넌 뭐하는 놈이니
         */

        return authentication.equals(CustomAuthenticationToken.class);
    }
}
