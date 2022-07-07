package com.example.loginservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Spring Security Context 내에 존재하는 인증 절차를 통과해야 함
     * 인증과정에서 실패하거나 인증 헤더(Authorization)을 보내지 않으면 401 Error
     * 이 과정을 여기서 처리함. 커스텀
     * Response가 401이 떨어질만한 에러가 발생하는 경우 해당 commence 로직을 타게 됨
     */

    private final ObjectMapper objectMapper;
    private static final String UNAUTHORIZED = "UnAuthorized";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized. message = {}, request url = {}", authException.getMessage(), request.getRequestURL());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    }
}
