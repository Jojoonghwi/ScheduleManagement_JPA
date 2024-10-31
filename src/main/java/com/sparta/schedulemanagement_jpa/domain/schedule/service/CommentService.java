package com.sparta.schedulemanagement_jpa.domain.schedule.service;

import static com.sparta.schedulemanagement_jpa.exception.enums.ExceptionCode.*;

import java.util.List;

import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.CommentRequestDto;
import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.CommentResponseDto;
import com.sparta.schedulemanagement_jpa.domain.schedule.entity.Comment;
import com.sparta.schedulemanagement_jpa.domain.schedule.entity.Schedule;
import com.sparta.schedulemanagement_jpa.domain.schedule.repository.CommentRepository;
import com.sparta.schedulemanagement_jpa.domain.schedule.repository.ScheduleRepository;
import com.sparta.schedulemanagement_jpa.domain.scheduleUser.entity.ScheduleUser;
import com.sparta.schedulemanagement_jpa.domain.scheduleUser.repository.ScheduleUserRepository;
import com.sparta.schedulemanagement_jpa.domain.user.entity.User;
import com.sparta.schedulemanagement_jpa.domain.user.entity.UserRole;
import com.sparta.schedulemanagement_jpa.domain.user.repository.UserRepository;
import com.sparta.schedulemanagement_jpa.domain.user.service.UserService;
import com.sparta.schedulemanagement_jpa.exception.customException.CommentExceptions;
import com.sparta.schedulemanagement_jpa.exception.customException.ScheduleExceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ScheduleUserRepository scheduleUserRepository;
    private final Schedule schedule;

    @Transactional
    public CommentResponseDto createComment(Long scheduleId, CommentRequestDto requestDto) {
        // RequestDto -> Entity
        Comment comment = new Comment(requestDto);

        //입력받은 일정의 아이디가 있는지 확인 후 저장
        Schedule schedule = findSchedule(scheduleId);
        comment.setSchedule(schedule);

        // DB 저장
        Comment saveComment = commentRepository.save(comment);

        // Entity -> ResponseDto
        CommentResponseDto CommentRepository = new CommentResponseDto(saveComment);

        return CommentRepository;
    }

    public Page<CommentResponseDto> getComments(int page, int size, String sortBy, boolean isAsc) {
        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Comment> comments = commentRepository.findAll(pageable);

        return comments.map(CommentResponseDto::new);
    }

    @Transactional
    public CommentResponseDto updateComment(Long CommentId, CommentRequestDto requestDto, HttpServletRequest request) {
        // 해당 일정이 DB에 존재하는지 확인
        Comment comment = findComment(CommentId);

        //인증된 사용자 정보를 request에서 가져오기
        String currentEmail = (String)request.getAttribute("email");
        UserRole currentRole = (UserRole) request.getAttribute("role");

        // ADMIN 또는 작성자만 수정 가능
        if (!currentRole.equals(UserRole.ADMIN)) {
            // currentEmail에 해당하는 User 조회
            userRepository.findByEmail(currentEmail).orElseThrow(() -> new ScheduleExceptions(NOT_FOUND_USER));

            boolean isCreator = false;
            List<ScheduleUser> scheduleUserList = schedule.getScheduleUserList();

            for(int i=0; i<scheduleUserList.size(); i++) {
                ScheduleUser scheduleUser = scheduleUserList.get(i);
                if(scheduleUser.getUser().getEmail().equals(currentEmail)) {
                    isCreator = true;
                }
            }

            if(!isCreator) {
                throw new ScheduleExceptions(HAS_NOT_PERMISSION);
            }
        }


        comment.update(requestDto);

        return new CommentResponseDto(comment);
    }

    public Long deleteComment(Long id) {
        // 해당 댓글이 DB에 존재하는지 확인
        Comment comment = findComment(id);

        commentRepository.delete(comment);

        return id;
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
            new CommentExceptions(NOT_FOUND_COMMENT));
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
            new ScheduleExceptions(NOT_FOUND_SCHEDULE));
    }
}
