package com.hhplus_tdd_arch.lecture.repository;

import com.hhplus_tdd_arch.lecture.domain.Enrollment;
import com.hhplus_tdd_arch.lecture.domain.Lecture;
import com.hhplus_tdd_arch.lecture.domain.Users;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    Optional<Enrollment> findByUserAndLecture(Users user, Lecture lecture);

}
