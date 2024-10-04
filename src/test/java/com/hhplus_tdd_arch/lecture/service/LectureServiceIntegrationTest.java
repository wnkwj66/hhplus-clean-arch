package com.hhplus_tdd_arch.lecture.service;

import com.hhplus_tdd_arch.lecture.domain.Lecture;
import com.hhplus_tdd_arch.lecture.domain.Users;
import com.hhplus_tdd_arch.lecture.repository.EnrollmentRepository;
import com.hhplus_tdd_arch.lecture.repository.LectureRepository;
import com.hhplus_tdd_arch.lecture.repository.UsersRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 데이터베이스 사용
public class LectureServiceIntegrationTest {
    @Autowired
    private LectureService lectureService;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private Lecture lecture;

    @BeforeEach
    void setUp() {
        // 특강 초기화
        lecture = new Lecture("Spring Boot Lecture", 30, 0);
        lectureRepository.save(lecture);

        // 사용자 생성 및 저장
        for (int i = 1; i <= 40; i++) {
            Users user = new Users((long) i,"User" + i);
            userRepository.save(user);
        }
    }
    @AfterEach
    void end() {
        enrollmentRepository.deleteAll(enrollmentRepository.findAll());
        userRepository.deleteAll(userRepository.findAll());
        lectureRepository.deleteAll(lectureRepository.findAll());
    }
    @Test
    void 동시에_40명_신청_통합테스트() throws InterruptedException, ExecutionException {
        // given
        int threadsNumber = 40; // 40명의 사용자가 동시에 신청


        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(threadsNumber);
        CountDownLatch latch = new CountDownLatch(threadsNumber); // 각 스레드가 끝날 때까지 대기하는 동기화 기법

        List<Users> users = userRepository.findAll();

        // when
        for (int i = 0; i < threadsNumber; i++) {
            final int index = i;
            executorService.execute(() -> {
                try {
                    String result = lectureService.enrollToLecture(users.get(index).getId(), lecture.getId());

                    if (result.equals("특강 신청이 완료되었습니다.")) {
                        successCount.incrementAndGet(); // 성공 시 카운트 증가
                    } else if(result.equals("이미 신청한 특강입니다.")) {
                        failCount.incrementAndGet();
                    } else if(result.equals("정원이 초과되었습니다.")) {
                        failCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown(); // 작업이 끝날 때마다 카운트 감소
                }
            });
        }

        latch.await(); // 모든 스레드가 작업을 끝낼 때까지 대기
        System.out.println("successCount : " + successCount);
        System.out.println("failCount : " + failCount);

        // then
        // 검증: 성공적으로 신청된 사용자 수는 30명이어야 함
        assertEquals(30, successCount.get());
        assertEquals(30, lecture.getCurrentAttendees());
    }

    @Test
    void 동일한유저_5번신청시_1번성공_테스트() throws Exception {
        // given
        int requestCnt = 5;
        Users user = userRepository.findByName("User1").get();

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        ExecutorService executorService = Executors.newFixedThreadPool(requestCnt);
        CountDownLatch latch = new CountDownLatch(requestCnt); // 각 스레드가 끝날 때까지 대기하는 동기화 기법

        // when
        for (int i = 0; i < requestCnt; i++) {
            executorService.execute(() -> {
                String result = lectureService.enrollToLecture(user.getId(), lecture.getId());

                if (result.equals("특강 신청이 완료되었습니다.")) {
                    successCount.incrementAndGet(); // 성공 시 카운트 증가
                } else if(result.equals("이미 신청한 특강입니다.")) {
                    failCount.incrementAndGet();
                }
            });
        }

        latch.await(); // 모든 스레드가 작업을 끝낼 때까지 대기
        // then
        System.out.println("successCount : " + successCount);
        System.out.println("failCount : " + failCount);
        System.out.println("등록된 개수 : " + enrollmentRepository.findByUserAndLecture(user , lecture).stream().count());
        assertEquals(1, successCount.get());
        assertEquals(4, failCount.get());
        assertEquals(1, enrollmentRepository.findByUserAndLecture(user,lecture).stream().count());

    }
}
