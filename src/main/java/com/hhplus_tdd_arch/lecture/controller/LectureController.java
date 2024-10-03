package com.hhplus_tdd_arch.lecture.controller;

import com.hhplus_tdd_arch.lecture.domain.Lecture;
import com.hhplus_tdd_arch.lecture.service.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lectures")
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    /**
     * 특강 조회
     */
    @GetMapping
    public List<Lecture> enrollToLecture() {
        return lectureService.searchLectures();
    }

    /**
     * 특강 신청
     */
    @PostMapping("/{lectureId}/enroll")
    public ResponseEntity<String> enrollToLecture(@RequestParam Long userId, @PathVariable Long lectureId) {
        String result = lectureService.enrollToLecture(userId, lectureId);
        return ResponseEntity.ok(result);
    }

}
