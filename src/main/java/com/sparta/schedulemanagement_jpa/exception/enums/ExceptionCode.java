package com.sparta.schedulemanagement_jpa.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    //----------유저----------
    //유저 이름
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"),
    NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "이름은 최대 4글자까지 가능합니다"),
    USERNAME_REQUIRED(HttpStatus.BAD_REQUEST, "이름이 누락되었습니다"),

    //이메일
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "해당 이메일이 존재합니다"),
    EMAIL_REQUIRED(HttpStatus.BAD_REQUEST, "이메일이 누락되었습니다"),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일입니다"),

    //패스워드
    PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "패스워드가 누락되었습니다"),
    NOT_MATCH_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 맞지 않습니다"),
    SAME_BEFORE_PASSWORD(HttpStatus.UNAUTHORIZED, "변경된 비밀번호가 이전과 같습니다"),
    //--------------------

    //권한
    HAS_NOT_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다"),
    ROLE_REQUIRED(HttpStatus.BAD_REQUEST, "ADMIN과 USER중 하나를 입력하세요"),

    //일정
    NOT_FOUND_SCHEDULE(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"),

    //댓글
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다"),

    //토큰
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "인증 토큰이 잘못되었거나 누락되었습니다"),

    HAS_NOT_COOKIE(HttpStatus.BAD_REQUEST, "Request has not cookie"),
    NOT_SUPPORT_ENCODING_COOKIE(HttpStatus.BAD_REQUEST, "Not support encoding cookie"),
    HAS_NOT_TOKEN(HttpStatus.BAD_REQUEST, "Request has not token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Token is expired"),
    NOT_SUPPORT_TOKEN(HttpStatus.UNAUTHORIZED, "Is not support token"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
