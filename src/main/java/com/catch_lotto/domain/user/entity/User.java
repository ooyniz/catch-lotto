package com.catch_lotto.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 보호
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false, length = 255, unique = true)
    private String username; // id or email

    @Column(nullable = false, length = 255)
    private String password;

    @Setter
    @Column(nullable = false, length = 255)
    private String nickname;

    @Setter
    @Column
    private Date birth;

    @Setter
    @Column
    private Character gender;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String username, String password, String nickname, Date birth, Character gender, Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
        this.role = (role != null) ? role : Role.USER;
    }

    // todo social login
}
