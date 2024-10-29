package com.sparta.schedulemanagement_jpa.domain.user.controller;

import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.LoginRequestDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.SignupRequestDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.UserRequestDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.UserResponseDto;
import com.sparta.schedulemanagement_jpa.domain.user.entity.User;
import com.sparta.schedulemanagement_jpa.domain.user.repository.UserRepository;
import com.sparta.schedulemanagement_jpa.domain.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // public UserController(UserService userService) {
    //     this.userService = userService;
    // }

    //회원가입으로 대체
    // @PostMapping("")
    // public UserResponseDto createUser(@Valid @RequestBody UserRequestDto requestDto) {
    //     return userService.createUser(requestDto);
    // }

    @GetMapping("/user")
    public Page<UserResponseDto> getComments(
            @RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return userService.getUser(page-1, size, "modifiedAt", false);
    }



    @PutMapping("/user/{UserId}")
    public Long updateUser(@PathVariable Long UserId, @RequestBody UserRequestDto requestDto) {
        return userService.updateUser(UserId, requestDto);
    }

    @DeleteMapping("/user/{UserId}")
    public Long deleteUser(@PathVariable Long UserId) {
        return userService.deleteUser(UserId);
    }

    @PostMapping("/auth/signup")
    public User signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    @PostMapping("/auth/login")
    public void login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
    }

    @PostMapping("/auth/logout")
    public void logout(HttpServletResponse response) {
        userService.logout(response);
    }
}
