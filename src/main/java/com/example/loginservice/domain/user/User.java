package com.example.loginservice.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String oauth2Id;
    private String name;
    private String email;
    private String gender;
    private String age;
    private String birthday;
    private String birthyear;
    private String mobile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email, String gender, String age, String birthday, String birthyear, String mobile, Role role) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.birthday = birthday;
        this.birthyear = birthyear;
        this.mobile = mobile;
        this.role = role;
    }

    public static User of(String name, String email, String gender, String age, String birthday, String birthyear, String mobile, Role role) {
        return User.builder()
                .age(age)
                .birthday(birthday)
                .birthyear(birthyear)
                .email(email)
                .gender(gender)
                .mobile(mobile)
                .name(name)
                .role(role)
                .build();
    }

}
