package com.hhplus_tdd_arch.lecture.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    // 생성자를 통한 객체 생성
    public Enrollment(Users user, Lecture lecture) {
        this.user = user;
        this.lecture = lecture;
    }
}