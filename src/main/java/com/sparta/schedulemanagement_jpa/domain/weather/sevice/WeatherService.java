package com.sparta.schedulemanagement_jpa.domain.weather.sevice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sparta.schedulemanagement_jpa.domain.weather.dto.WeatherResponse;

import lombok.Setter;

@Service
@Setter
public class WeatherService {
	private final RestTemplate restTemplate = new RestTemplate();
	private static final String WEATHER_API_URL = "https://f-api.github.io/f-api/weather.json";

	public String getWeather(LocalDateTime date) {
		try {
			// 날짜 포맷팅
			String formattedDate = date.format(DateTimeFormatter.ofPattern("MM-dd"));

			// API 데이터 받아오기
			WeatherResponse[] responseArray = restTemplate.getForObject(WEATHER_API_URL, WeatherResponse[].class);

			if (responseArray != null) {
				// 날짜별 날씨 확인
				for (WeatherResponse response : responseArray) {
					if (response.getDate().equals(formattedDate)) { // date를 사용
						return response.getWeather();
					}
				}
			}
			return "날씨 정보 없음";
		} catch (Exception e) {
			return "날씨 조회 중 오류 발생";
		}
	}
}
