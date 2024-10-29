package com.sparta.schedulemanagement_jpa.domain.schedule.controller;

import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.ScheduleRequestDto;
import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.ScheduleResponseDto;
import com.sparta.schedulemanagement_jpa.domain.schedule.service.ScheduleService;
import com.sparta.schedulemanagement_jpa.domain.security.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

	private final ScheduleService scheduleService;
	private final JwtUtil jwtUtil;

	// public ScheduleController(ScheduleService scheduleService) {
	//     this.scheduleService = scheduleService;
	// }

	@PostMapping("/{id}")
	public ScheduleResponseDto createSchedule(@Valid @PathVariable Long id,
		@RequestBody ScheduleRequestDto requestDto) {
		return scheduleService.createSchedule(id, requestDto);
	}

	@GetMapping("")
	public Page<ScheduleResponseDto> getSchedules(
		@RequestParam("page") int page,
		@RequestParam(value = "size", defaultValue = "10") int size
	) {
		return scheduleService.getSchedules(page - 1, size, "modifiedAt", false);
	}

	// //일정 수정 or 관리자 업데이트
	// @PutMapping({"/{scheduleId}"})
	// public ScheduleResponseDto updateSchedule(@PathVariable Long scheduleId,
	// 	@RequestBody ScheduleRequestDto requestDto) {
	//
	// 	return scheduleService.updateSchedule(scheduleId, requestDto);
	// }

	@PutMapping("/{scheduleId}")
	public ScheduleResponseDto updateSchedule(
		@PathVariable Long scheduleId,
		@RequestBody ScheduleRequestDto requestDto,
		HttpServletRequest request
	) {
		// 쿠키에서 토큰 가져오기
		String token = jwtUtil.getTokenFromRequest(request);
		if (token == null || !jwtUtil.validateToken(jwtUtil.substringToken(token))) {
			throw new IllegalArgumentException("Invalid or missing Authorization token");
		}

		// // 유효한 토큰에서 사용자 정보 및 role 정보 추출
		// Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(token));
		// String currentEmail = String.valueOf(claims.getSubject());


		// 토큰에서 사용자 이메일과 role 가져오기
		Claims claims = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(token));
		String currentEmail = claims.getSubject(); // 이메일 정보
		String currentRole = claims.get(JwtUtil.AUTHORIZATION_KEY, String.class); // role 정보

		// 일정 업데이트 로직
		return scheduleService.updateSchedule(scheduleId, currentRole, currentEmail, requestDto);
	}


	@DeleteMapping("{id}")
	public Long deleteSchedule(@PathVariable Long id) {
		return scheduleService.deleteSchedule(id);
	}
}
