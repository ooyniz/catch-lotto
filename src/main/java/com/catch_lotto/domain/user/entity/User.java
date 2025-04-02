package com.catch_lotto.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false, length = 255, unique = true)
    private String username; // id or email

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private String nickname;

    @Column
    private Date birth;

    @Column
    private Character gender;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public User(String username, String password, String nickname, Date birth, Character gender) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
    }
    // todo social login
}
