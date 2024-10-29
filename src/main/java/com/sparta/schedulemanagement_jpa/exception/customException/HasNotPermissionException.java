package com.sparta.schedulemanagement_jpa.exception.customException;

import com.sparta.schedulemanagement_jpa.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class HasNotPermissionException extends RuntimeException {
    public final ExceptionCode exceptionCode;

    public HasNotPermissionException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
