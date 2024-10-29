package com.sparta.schedulemanagement_jpa.domain.scheduleUser.entity;

import com.sparta.schedulemanagement_jpa.domain.Timestamped;
import com.sparta.schedulemanagement_jpa.domain.schedule.entity.Schedule;
import com.sparta.schedulemanagement_jpa.domain.user.entity.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Table(name = "schedule_user") // 매핑할 테이블의 이름을 지정
@Getter
@Setter
@NoArgsConstructor
public class ScheduleUser extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
