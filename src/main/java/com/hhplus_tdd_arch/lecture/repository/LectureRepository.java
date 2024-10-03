package com.hhplus_tdd_arch.lecture.repository;

import com.hhplus_tdd_arch.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

}
