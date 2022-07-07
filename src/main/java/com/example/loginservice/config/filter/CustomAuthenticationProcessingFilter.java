package com.example.loginservice.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * UsernamePasswordAuthenticationProcessingFilter 대신 이것이 작동하게 될 것.
     * 사용자의 요청이 가장 먼저 도달하는 인증 필터
     * 여기서 요청을 처리하고, Authentication을 꺼내는 작업을 수행
     * 여기서 Authentication을 꺼내서 Authentication Manager에 전달할 것.
     * Authentication Manager는 진짜인지 검증을 할 것.
     * 즉 여기서는 객체만 얻어서 날리고 검증까지는 하지 않음
     */

    private static final String LOGIN_URL = "/api/v1/login"; // 이 url에서 오는 요청을 처리할 것이다
    private static final String METHOD_NOT_SUPPORTED_MESSAGE = "Authentication method not supported";

    @Autowired
    private ObjectMapper objectMapper;

    public CustomAuthenticationProcessingFilter(AccessTokenAuthenticationProvider)

    public CustomAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher(LOGIN_URL, HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(!HttpMethod.POST.matches(request.getMethod())) {
            log.error("Authentication method not supported. Request Method = {}", request.getMethod());
            throw new AuthenticationServiceException(METHOD_NOT_SUPPORTED_MESSAGE);
        }
    }
}
