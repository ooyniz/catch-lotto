package com.catch_lotto.domain.user.repository;

import com.catch_lotto.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String userName);
    boolean existsByUsername(String username);
}
