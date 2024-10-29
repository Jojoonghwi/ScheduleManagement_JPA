package com.sparta.schedulemanagement_jpa.domain.schedule.controller;

import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.CommentRequestDto;
import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.CommentResponseDto;
import com.sparta.schedulemanagement_jpa.domain.schedule.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{id}")
    public CommentResponseDto createComment(@Valid @PathVariable Long id, @RequestBody CommentRequestDto requestDto) {
        return commentService.createComment(id, requestDto);
    }

    @GetMapping("")
    public Page<CommentResponseDto> getComments(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return commentService.getComments(page-1, size, "modifiedAt", false);
    }

    @PutMapping("{id}")
    public Long updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto) {
        return commentService.updateComment(id, requestDto);
    }

    @DeleteMapping("{id}")
    public Long deleteComment(@PathVariable Long id) {
        return commentService.deleteComment(id);
    }
}
