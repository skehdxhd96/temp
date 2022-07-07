package com.example.loginservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    private static final String CORS_PATTERN = "/**";
    private static final String CLIENT_URL = "/**"; /* http://localhost:3000 */
    private static final String[] COR_ALLOWED_METHODS = {"GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONs"};

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedMethod(CLIENT_URL);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowedMethods(Arrays.asList(COR_ALLOWED_METHODS));
        configuration.setAllowCredentials(true); // cross origin으로부터 쿠키 정보 받을지 여부

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(CORS_PATTERN, configuration);

        return source;
    }
}
