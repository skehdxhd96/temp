package com.example.loginservice.config;

import com.example.loginservice.config.filter.CustomAuthenticationProcessingFilter;
import com.example.loginservice.config.handler.CustomAccessDeniedHandler;
import com.example.loginservice.config.handler.CustomAuthenticationFailureHandler;
import com.example.loginservice.config.handler.CustomAuthenticationSuccessHandler;
import com.example.loginservice.domain.user.Role;
import com.example.loginservice.oauth2.NaverOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.FilterInvocation;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private static final String[] GET_PERMITTED_URLS = {
//
//    };
//    private static final String[] POST_PERMITTED_URLS = {
//
//    };

    private final CorsConfigurationSource corsConfigurationSource;
    private final NaverOAuth2UserService naverOAuth2UserService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource)
                .and()
                .csrf().disable() // rest api 방식으로 통신할 것이므로 csrf 꺼도 된다
                .formLogin().disable() // SSR 사용하지 않음
                .httpBasic().disable() // Http basic Auth 기반 인증창. 사용하지 않을 것
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) jwt와 관련된 부분
                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, GET_PERMITTED_URLS).permitAll()
//                .antMatchers(HttpMethod.POST, POST_PERMITTED_URLS).permitAll()
                .antMatchers("/api/v1/seller/**").hasRole(Role.SELLER.name())
                .antMatchers("/api/v1/customer/**").hasRole(Role.CUSTOMER.name())
                .anyRequest().authenticated();

        http
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);


        http
                .oauth2Login() // default : UsernamePasswordAuthenticationFilter가 동작하지만 Oauth2로그인을 연동하면 Oauth2LoginAuthenticationFilter가 동작함. 가능한 이유는 둘 다 AbstractAuthenticationProcessingFilter의 구현체이기 때문
                .userInfoEndpoint()
                .userService(naverOAuth2UserService);
//        http.httpBasic().disable();

    }

    @Bean
    public CustomAuthenticationProcessingFilter customAuthenticationProcessingFilter() throws Exception {
        CustomAuthenticationProcessingFilter customAuthenticationProcessingFilter = new CustomAuthenticationProcessingFilter();
        customAuthenticationProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        customAuthenticationProcessingFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        customAuthenticationProcessingFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        return customAuthenticationProcessingFilter;
    }
//
//    private SecurityExpressionHandler<FilterInvocation> configExpressionHandler() {
//        /* 무야 이건 */
//    }
}
