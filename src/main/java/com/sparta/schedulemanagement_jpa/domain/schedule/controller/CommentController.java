package com.sparta.schedulemanagement_jpa.domain.schedule.controller;

import static org.springframework.data.jpa.domain.AbstractPersistable_.*;

import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.CommentRequestDto;
import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.CommentResponseDto;
import com.sparta.schedulemanagement_jpa.domain.schedule.service.CommentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<CommentResponseDto> createComment(@Valid @PathVariable Long id, @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto commentResponseDto = commentService.createComment(id, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
    }

    @GetMapping("")
    public ResponseEntity<Page<CommentResponseDto>> getComments(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<CommentResponseDto> commentResponseDtos = commentService.getComments(page-1, size, "modifiedAt", false);

        return ResponseEntity.status(HttpStatus.OK).body(commentResponseDtos);
    }

    @PutMapping("{CommentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long CommentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        CommentResponseDto commentResponseDto = commentService.updateComment(CommentId, requestDto, request);

        return ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);
    }

    @DeleteMapping("{CommentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Long deleteComment(@PathVariable Long CommentId) {
        return commentService.deleteComment(CommentId);
    }
}
