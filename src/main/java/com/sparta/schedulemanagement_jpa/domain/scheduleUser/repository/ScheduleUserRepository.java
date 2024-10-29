package com.sparta.schedulemanagement_jpa.domain.scheduleUser.repository;

import java.util.Optional;

import com.sparta.schedulemanagement_jpa.domain.scheduleUser.entity.ScheduleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sparta.schedulemanagement_jpa.domain.schedule.entity.Schedule;

public interface ScheduleUserRepository extends JpaRepository<ScheduleUser, Long> {
	Optional<ScheduleUser> findByScheduleIdAndUserId(Long scheduleId, Long userId);
}
