package com.example.loginservice.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String nickname;
    private String email;
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId; // 소셜로그인에서 발급받는 아이디디

    @Builder
    public User(Long id, String name, String nickname, String email,
                Role role, AuthProvider provider, String providerId) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void changeUserInfo(String nickname, String name) {
        this.nickname = nickname;
        this.name = name;
    }

    public String getRollName() {
        return role.name();
    }
}
