package com.hhplus_tdd_arch.lecture.service;

import com.hhplus_tdd_arch.lecture.domain.Enrollment;
import com.hhplus_tdd_arch.lecture.domain.Lecture;
import com.hhplus_tdd_arch.lecture.domain.Users;
import com.hhplus_tdd_arch.lecture.repository.EnrollmentRepository;
import com.hhplus_tdd_arch.lecture.repository.LectureRepository;
import com.hhplus_tdd_arch.lecture.repository.UsersRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final UsersRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    public LectureService(LectureRepository lectureRepository, UsersRepository userRepository, EnrollmentRepository enrollmentRepository) {
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * 특강 신청 기능
     */

    @Transactional
    public String enrollToLecture(Long userId, Long lectureId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        // 이미 등록된 경우 처리
        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByUserAndLecture(user, lecture);
        if (existingEnrollment.isPresent()) {
            System.out.println("이미 신청한 특강입니다.X");
            return "이미 신청한 특강입니다.";
        }

        // 정원이 초과된 경우 처리
        if (lecture.getCurrentAttendees() >= lecture.getMaxAttendees()) {
            System.out.println("정원이 초과되었습니다.");
            return "정원이 초과되었습니다.";
        }

        // Enrollment 객체 생성
        Enrollment enrollment = new Enrollment(user, lecture);
        enrollmentRepository.save(enrollment);

        // 현재 참가자 수 증가
        lecture.increaseCurrentAttendees();
        lectureRepository.save(lecture);
        System.out.println("특강 신청이 완료되었습니다.");
        return "특강 신청이 완료되었습니다.";
    }

    /**
     * 강의 조회 기능
     */
    @Transactional(readOnly = true)
    public List<Lecture> searchLectures() {
        return lectureRepository.findAll();
    }
}
