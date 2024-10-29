package com.sparta.schedulemanagement_jpa.domain.weather.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class WeatherResponse {
	private String weather;
	private String date;  // 날짜를 String으로 추가

	public WeatherResponse(String weather, String date) {
		this.weather = weather;
		this.date = date; // 날짜 초기화
	}
}
