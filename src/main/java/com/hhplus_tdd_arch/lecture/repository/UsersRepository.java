package com.hhplus_tdd_arch.lecture.repository;

import com.hhplus_tdd_arch.lecture.domain.Users;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByName(String name);

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Users> findById(Long id);
}
