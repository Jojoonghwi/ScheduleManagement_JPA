package com.sparta.schedulemanagement_jpa.domain.user.controller;

import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.LoginRequestDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.SignupRequestDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.SignupResponseDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.UserUpdateRequestDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.UserResponseDto;
import com.sparta.schedulemanagement_jpa.domain.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {

        SignupResponseDto userResponseDto = userService.signup(signupRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<SignupResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        SignupResponseDto userResponseDto = userService.login(loginRequestDto, response);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponseDto);
    }

    @PostMapping("/auth/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        userService.logout(response);
    }

    @GetMapping("/user")
    public ResponseEntity <Page<UserResponseDto>> getUsers(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<UserResponseDto> userResponseDto = userService.getUser(page-1, size, "modifiedAt", false);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @PutMapping("/user/{UserId}")
    public ResponseEntity<UserResponseDto> updateUser(
        @PathVariable Long UserId, @RequestBody UserUpdateRequestDto userUpdateRequestDto, HttpServletRequest request) {

        UserResponseDto userResponseDto = userService.updateUser(UserId, userUpdateRequestDto, request);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @DeleteMapping("/user/{UserId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Long deleteUser(@PathVariable Long UserId) {
        return userService.deleteUser(UserId);
    }
}
