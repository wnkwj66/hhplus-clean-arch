package com.hhplus_tdd_arch.lecture.repository;

import com.hhplus_tdd_arch.lecture.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String name);
}
