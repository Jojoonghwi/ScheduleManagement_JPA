package com.sparta.schedulemanagement_jpa.domain.schedule.controller;

import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.ScheduleRequestDto;
import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.ScheduleResponseDto;
import com.sparta.schedulemanagement_jpa.domain.schedule.service.ScheduleService;
import com.sparta.schedulemanagement_jpa.domain.security.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

	private final ScheduleService scheduleService;
	private final JwtUtil jwtUtil;

	@PostMapping("/{id}")
	public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @PathVariable Long id,
		@RequestBody ScheduleRequestDto requestDto) {

		ScheduleResponseDto scheduleResponseDto = scheduleService.createSchedule(id, requestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(scheduleResponseDto);
	}

	@GetMapping("")
	public ResponseEntity<Page<ScheduleResponseDto>> getSchedules(
		@RequestParam("page") int page,
		@RequestParam(value = "size", defaultValue = "10") int size
	) {

		Page<ScheduleResponseDto> scheduleResponseDtos = scheduleService.getSchedules(page - 1, size, "modifiedAt", false);
		return ResponseEntity.status(HttpStatus.OK).body(scheduleResponseDtos);
	}

	@PutMapping("/{scheduleId}")
	public ResponseEntity<ScheduleResponseDto> updateSchedule(
		@PathVariable Long scheduleId,
		@RequestBody ScheduleRequestDto requestDto,
		HttpServletRequest request
	) {

		ScheduleResponseDto scheduleResponseDto = scheduleService.updateSchedule(scheduleId, request, requestDto);

		return ResponseEntity.status(HttpStatus.OK).body(scheduleResponseDto);
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Long deleteSchedule(@PathVariable Long id) {
		return scheduleService.deleteSchedule(id);
	}
}
