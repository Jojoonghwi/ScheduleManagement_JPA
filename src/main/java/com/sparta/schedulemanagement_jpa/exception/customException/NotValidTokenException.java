package com.sparta.schedulemanagement_jpa.exception.customException;

import com.sparta.schedulemanagement_jpa.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class NotValidTokenException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public NotValidTokenException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
