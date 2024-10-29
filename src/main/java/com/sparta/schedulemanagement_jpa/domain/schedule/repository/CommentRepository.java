package com.sparta.schedulemanagement_jpa.domain.schedule.repository;

import com.sparta.schedulemanagement_jpa.domain.schedule.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByOrderByModifiedAtDesc();
}
