package com.hhplus_tdd_arch.lecture.service;

import com.hhplus_tdd_arch.lecture.domain.Enrollment;
import com.hhplus_tdd_arch.lecture.domain.Lecture;
import com.hhplus_tdd_arch.lecture.domain.Users;
import com.hhplus_tdd_arch.lecture.repository.EnrollmentRepository;
import com.hhplus_tdd_arch.lecture.repository.LectureRepository;
import com.hhplus_tdd_arch.lecture.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LectureServiceTest {
    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private UsersRepository userRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private LectureService lectureService;

    private Users user;
    private Lecture lecture;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new Users("Tester");
        lecture = new Lecture("Spring Boot Lecture", 30, 0);
    }
    @Test
    void 특강_신청_성공() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(enrollmentRepository.findByUserAndLecture(user, lecture)).thenReturn(Optional.empty());

        Enrollment enrollment = new Enrollment(user, lecture);

        // when
        String result = lectureService.enrollToLecture(1L, 1L);

        // then
        assertEquals("특강 신청이 완료되었습니다.", result);
        verify(enrollmentRepository).save(any(Enrollment.class));
        verify(lectureRepository).save(any(Lecture.class));
        assertEquals(1, lecture.getCurrentAttendees());
    }
    @Test
    void 특강_신청_실패_중복신청() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(enrollmentRepository.findByUserAndLecture(user, lecture)).thenReturn(Optional.of(new Enrollment(user, lecture)));

        // when
        String result = lectureService.enrollToLecture(1L, 1L);

        // then
        assertEquals("이미 신청한 특강입니다.", result);
        verify(enrollmentRepository, never()).save(any());
        verify(lectureRepository, never()).save(any());
    }

    @Test
    void 특강_신청_실패_정원초과() {
        // given
        Lecture fullLecture = new Lecture("Spring Boot Lecture", 30, 30);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(fullLecture));
        when(enrollmentRepository.findByUserAndLecture(user, fullLecture)).thenReturn(Optional.empty());

        // when
        String result = lectureService.enrollToLecture(1L, 1L);

        // then
        assertEquals("정원이 초과되었습니다.", result);
        verify(enrollmentRepository, never()).save(any());
        verify(lectureRepository, never()).save(any());
    }
}