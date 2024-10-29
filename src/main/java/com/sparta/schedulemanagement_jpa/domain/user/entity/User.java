package com.sparta.schedulemanagement_jpa.domain.user.entity;

import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.UserRequestDto;
import com.sparta.schedulemanagement_jpa.domain.scheduleUser.entity.ScheduleUser;
import com.sparta.schedulemanagement_jpa.domain.Timestamped;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
//@Table(name = "user") // 매핑할 테이블의 이름을 지정
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})//중복 허용 x
@Getter
@Setter
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private com.sparta.schedulemanagement_jpa.domain.user.entity.UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ScheduleUser> ScheduleUserList = new ArrayList<>();

    public User(String username, String email, String password, com.sparta.schedulemanagement_jpa.domain.user.entity.UserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void update(UserRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
    }

}
