package com.hhplus_tdd_arch.lecture.repository;

import com.hhplus_tdd_arch.lecture.domain.Enrollment;
import com.hhplus_tdd_arch.lecture.domain.Lecture;
import com.hhplus_tdd_arch.lecture.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserAndLecture(Users user, Lecture lecture);
}
