package com.sparta.schedulemanagement_jpa.exception.customException;

import com.sparta.schedulemanagement_jpa.exception.enums.ExceptionCode;

import lombok.Getter;

@Getter
public class CommentExceptions extends RuntimeException {
	private final ExceptionCode exceptionCode;

	public CommentExceptions(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
