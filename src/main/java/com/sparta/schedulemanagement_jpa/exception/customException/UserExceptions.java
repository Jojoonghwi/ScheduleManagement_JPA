package com.sparta.schedulemanagement_jpa.exception.customException;

import com.sparta.schedulemanagement_jpa.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class UserExceptions extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public UserExceptions(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
