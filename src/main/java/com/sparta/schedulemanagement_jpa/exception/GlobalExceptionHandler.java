package com.sparta.schedulemanagement_jpa.exception;

import static com.sparta.schedulemanagement_jpa.exception.dto.NotValidRequestParameter.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sparta.schedulemanagement_jpa.exception.customException.*;
import com.sparta.schedulemanagement_jpa.exception.dto.NotValidRequestParameter;
import com.sparta.schedulemanagement_jpa.exception.dto.ResponseExceptionCode;
import com.sparta.schedulemanagement_jpa.exception.enums.ExceptionCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "ControllerException")
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserExceptions.class)
    public ResponseEntity<Object> handleUserExceptions(UserExceptions e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}: {}", exceptionCode, exceptionCode.getMessage());
        return ResponseEntity.status(exceptionCode.getHttpStatus())
            .body(makeResponseExceptionCode(exceptionCode));
    }

    @ExceptionHandler(ScheduleExceptions.class)
    public ResponseEntity<Object> handleScheduleExceptions(ScheduleExceptions e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}: {}", exceptionCode, exceptionCode.getMessage());
        return ResponseEntity.status(exceptionCode.getHttpStatus())
            .body(makeResponseExceptionCode(exceptionCode));
    }

    @ExceptionHandler(CommentExceptions.class)
    public ResponseEntity<Object> handleCommentExceptions(CommentExceptions e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}: {}", exceptionCode, exceptionCode.getMessage());
        return ResponseEntity.status(exceptionCode.getHttpStatus())
            .body(makeResponseExceptionCode(exceptionCode));
    }

    @ExceptionHandler(NotValidCookieException.class)
    public ResponseEntity<Object> handleNotValidCookieException(NotValidCookieException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}: {}", exceptionCode, exceptionCode.getMessage());
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(makeResponseExceptionCode(exceptionCode));
    }

    @ExceptionHandler(NotValidTokenException.class)
    public ResponseEntity<Object> handleNotValidTokenException(NotValidTokenException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}: {}", exceptionCode, exceptionCode.getMessage());
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(makeResponseExceptionCode(exceptionCode));
    }

    @ExceptionHandler(HasNotPermissionException.class)
    public ResponseEntity<Object> handleHasNotPermissionException(HasNotPermissionException e) {
        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("{}: {}", exceptionCode, exceptionCode.getMessage());
        return ResponseEntity.status(exceptionCode.getHttpStatus())
                .body(makeResponseExceptionCode(exceptionCode));
    }

    private ResponseExceptionCode makeResponseExceptionCode(ExceptionCode exceptionCode) {
        return ResponseExceptionCode.builder()
                .code(exceptionCode.name())
                .message(exceptionCode.getMessage())
                .build();
    }

    private NotValidRequestParameter makeNotValidRequestParameter(BindException e,
                                                                  ExceptionCode exceptionCode) {
        List<NotValidParameter> notValidParameters = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(NotValidParameter::of)
                .toList();

        return NotValidRequestParameter.builder()
                .code(exceptionCode.name())
                .message(exceptionCode.getMessage())
                .notValidParameters(notValidParameters)
                .build();
    }
}
