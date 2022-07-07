//package com.example.loginservice.config.userdetails;
//
//import com.example.loginservice.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service("userDetailsService")
//public class CustomUserDetailsService implements UserDetailsService {
//
//    /**
//     * 넘어온 데이터를 이용해 UserDetails 객체를 반환
//     * UserDetails을 AuthenticationProvider로 보낸다.
//     * 그럼 AuthenticationProvider에서 여기서 넘겨준 UserDetails로 검증할 것.
//     */
//
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        /**
//         * 진짜 username이 아니고 userId(PK)임
//         */
//
//        userRepository.findById(Long.parseLong(username));
//    }
//}
