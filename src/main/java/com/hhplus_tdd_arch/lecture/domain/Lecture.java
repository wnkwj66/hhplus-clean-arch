package com.hhplus_tdd_arch.lecture.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "maxAttendees", nullable = false)
    private int maxAttendees;

    @Column(name = "currentAttendees", nullable = false)
    private int currentAttendees = 0;

    // 생성자를 통한 객체 생성
    public Lecture(String title, int maxAttendees, int currentAttendees) {
        this.title = title;
        this.maxAttendees = maxAttendees;
        this.currentAttendees = currentAttendees;
    }

    // 현재 참가자 수 증가
    public void increaseCurrentAttendees() {
        this.currentAttendees++;
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }

    public int getCurrentAttendees() {
        return currentAttendees;
    }
}
