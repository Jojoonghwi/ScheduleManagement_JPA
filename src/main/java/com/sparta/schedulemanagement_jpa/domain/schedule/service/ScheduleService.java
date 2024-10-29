package com.sparta.schedulemanagement_jpa.domain.schedule.service;

import static com.sparta.schedulemanagement_jpa.exception.enums.ExceptionCode.*;

import java.time.LocalDateTime;

import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.ScheduleRequestDto;
import com.sparta.schedulemanagement_jpa.domain.schedule.controller.dto.ScheduleResponseDto;
import com.sparta.schedulemanagement_jpa.domain.schedule.entity.Schedule;
import com.sparta.schedulemanagement_jpa.domain.scheduleUser.entity.ScheduleUser;
import com.sparta.schedulemanagement_jpa.domain.security.jwt.JwtUtil;
import com.sparta.schedulemanagement_jpa.domain.user.entity.User;
import com.sparta.schedulemanagement_jpa.domain.schedule.repository.ScheduleRepository;
import com.sparta.schedulemanagement_jpa.domain.scheduleUser.repository.ScheduleUserRepository;
import com.sparta.schedulemanagement_jpa.domain.user.repository.UserRepository;
import com.sparta.schedulemanagement_jpa.domain.weather.sevice.WeatherService;
import com.sparta.schedulemanagement_jpa.exception.customException.ScheduleExceptions;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ScheduleUserRepository scheduleUserRepository;
    private final JwtUtil jwtUtil;
    private final WeatherService weatherService;

    public ScheduleResponseDto createSchedule(Long userId, ScheduleRequestDto requestDto) {
        // RequestDto -> Entity
        Schedule schedule = new Schedule(requestDto);

        User user = userRepository.findById(userId).orElseThrow(() -> new ScheduleExceptions(NOT_FOUND_USER));

        //날씨 저장
        String weather = weatherService.getWeather(LocalDateTime.now());
        schedule.setWeather(weather);

        // DB 저장
        Schedule saveSchedule = scheduleRepository.save(schedule);

        ScheduleUser scheduleUser = new ScheduleUser();
        scheduleUser.setSchedule(saveSchedule);
        scheduleUser.setUser(user);

        scheduleUserRepository.save(scheduleUser);

        // Entity -> ResponseDto
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(saveSchedule);

        return scheduleResponseDto;
    }

    public Page<ScheduleResponseDto> getSchedules(int page, int size, String sortBy, boolean isAsc) {
        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Schedule> Schedules = scheduleRepository.findAll(pageable);

        return Schedules.map(ScheduleResponseDto::new);
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long scheduleId, String currentRole, String currentEmail, ScheduleRequestDto requestDto) {
        // 해당 일정이 DB에 존재하는지 확인
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleExceptions(NOT_FOUND_SCHEDULE));

        // ADMIN 또는 작성자만 수정 가능 수정 가능
        if (!currentRole.equals("ADMIN")) {
            // currentEmail에 해당하는 User 조회
            User user = userRepository.findByEmail(currentEmail).orElseThrow(() -> new ScheduleExceptions(NOT_FOUND_USER));

            //현재 사용자가 schedule과 연관된 사용자인지 확인
            boolean isCreator = scheduleUserRepository
                .findByScheduleIdAndUserId(scheduleId, user.getId())
                .isPresent();

            if (!isCreator) {
                throw new ScheduleExceptions(HAS_NOT_PERMISSION);
            }
        }

        schedule.update(requestDto);

        Schedule updateSchedule =  scheduleRepository.save(schedule);

        return new ScheduleResponseDto(updateSchedule);
    }

    public Long deleteSchedule(Long id) {
        // 해당 일정이 DB에 존재하는지 확인
        Schedule schedule = findSchedule(id);

        scheduleRepository.delete(schedule);

        return id;
    }

    private Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() ->
                new ScheduleExceptions(NOT_FOUND_SCHEDULE));
    }
}
