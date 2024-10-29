package com.sparta.schedulemanagement_jpa.domain.user.service;

import static com.sparta.schedulemanagement_jpa.domain.security.jwt.JwtUtil.*;
import static com.sparta.schedulemanagement_jpa.exception.enums.ExceptionCode.*;

import java.util.Optional;

import com.sparta.schedulemanagement_jpa.domain.security.config.PasswordEncoder;
import com.sparta.schedulemanagement_jpa.domain.security.jwt.JwtUtil;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.LoginRequestDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.SignupRequestDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.UserRequestDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.UserResponseDto;
import com.sparta.schedulemanagement_jpa.domain.user.entity.User;
import com.sparta.schedulemanagement_jpa.domain.user.entity.UserRole;
import com.sparta.schedulemanagement_jpa.domain.user.repository.UserRepository;
import com.sparta.schedulemanagement_jpa.exception.customException.UserExceptions;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Page<UserResponseDto> getUser(int page, int size, String sortBy, boolean isAsc) {
        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> Users = userRepository.findAll(pageable);

        return Users.map(UserResponseDto::new);
    }

    @Transactional
    public Long updateUser(Long UserId, UserRequestDto requestDto) {
        // 해당 유저가 DB에 존재하는지 확인
        User user = findUser(UserId);

        user.update(requestDto);

        return UserId;
    }

    public Long deleteUser(Long UserId) {
        // 해당 유저가 DB에 존재하는지 확인
        User user = findUser(UserId);

        userRepository.delete(user);

        return UserId;
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserExceptions(NOT_FOUND_USER)
        );
    }

    public User signup(@Valid SignupRequestDto signupRequestDto) {
        return validateSignupRequest(signupRequestDto);
    }

    public void login(@Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserExceptions(NOT_FOUND_USER));//401

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserExceptions(NOT_MATCH_PASSWORD);//401
        }

        String token = jwtUtil.createToken(email, user.getRole());
        jwtUtil.addJwtToCookie(token, response);
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, null); //쿠키 생성 후 null -> 삭제
        cookie.setHttpOnly(true); // JavaScript에 의해 접근되지 않도록
        cookie.setMaxAge(0); // 유효 시간 0
        cookie.setPath("/"); // 모든 경로에서 유효
        response.addCookie(cookie);
    }

    private User validateSignupRequest(SignupRequestDto signupRequestDto) {
        //유저 이름
        String username = signupRequestDto.getUsername();
        if(username.length() > 4) {
            throw new UserExceptions(NAME_TOO_LONG);
        }
        if(username.isEmpty()) {
            throw new UserExceptions(USERNAME_REQUIRED);
        }

        //이메일
        String email = signupRequestDto.getEmail();
        if(email.isEmpty()) {
            throw new UserExceptions(EMAIL_REQUIRED);
        }
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new UserExceptions(DUPLICATE_EMAIL);
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if(!email.matches(emailRegex)) {
            throw new UserExceptions(INVALID_EMAIL_FORMAT);
        }

        //패스워드
        if(signupRequestDto.getPassword().isEmpty() || signupRequestDto.getPassword().equals(" ")) {
            throw new UserExceptions(PASSWORD_REQUIRED);
        }
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        //권한
        String role = signupRequestDto.getRole();
        if (!(role.equals("ADMIN") || role.equals("USER"))) {
            throw new UserExceptions(ROLE_REQUIRED);
        }
        UserRole userRole = UserRole.valueOf(role);

        User user = new User(username, email, password, userRole);
        userRepository.save(user);

        return user;
    }
}
