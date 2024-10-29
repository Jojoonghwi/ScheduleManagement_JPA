package com.sparta.schedulemanagement_jpa.domain.schedule.repository;

import com.sparta.schedulemanagement_jpa.domain.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByOrderByModifiedAtDesc();
    Page<Schedule> findAll(Pageable pageable);
}
//findAllByOrderByModifiedAtDesc