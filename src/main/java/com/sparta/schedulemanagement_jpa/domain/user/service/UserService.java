package com.sparta.schedulemanagement_jpa.domain.user.service;

import static com.sparta.schedulemanagement_jpa.domain.security.jwt.JwtUtil.*;
import static com.sparta.schedulemanagement_jpa.exception.enums.ExceptionCode.*;

import java.util.Optional;

import com.sparta.schedulemanagement_jpa.domain.security.config.PasswordEncoder;
import com.sparta.schedulemanagement_jpa.domain.security.jwt.JwtUtil;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.LoginRequestDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.SignupRequestDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.SignupResponseDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.UserRequestDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.UserUpdateRequestDto;
import com.sparta.schedulemanagement_jpa.domain.user.controller.dto.UserResponseDto;
import com.sparta.schedulemanagement_jpa.domain.user.entity.User;
import com.sparta.schedulemanagement_jpa.domain.user.entity.UserRole;
import com.sparta.schedulemanagement_jpa.domain.user.repository.UserRepository;
import com.sparta.schedulemanagement_jpa.exception.customException.ScheduleExceptions;
import com.sparta.schedulemanagement_jpa.exception.customException.UserExceptions;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    public SignupResponseDto signup(@Valid SignupRequestDto signupRequestDto) {
        User user = validateUserRequest(signupRequestDto, true);

        return new SignupResponseDto(user);
    }

    public SignupResponseDto login(@Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        validateUserRequest(loginRequestDto, false);


        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserExceptions(NOT_FOUND_USER));//401

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserExceptions(NOT_MATCH_PASSWORD);//401
        }

        String token = jwtUtil.createToken(email, user.getRole());
        jwtUtil.addJwtToCookie(token, response);

        return new SignupResponseDto(user);
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, null); //쿠키 생성 후 null -> 삭제
        cookie.setHttpOnly(true); // JavaScript에 의해 접근되지 않도록
        cookie.setMaxAge(0); // 유효 시간 0
        cookie.setPath("/"); // 모든 경로에서 유효
        response.addCookie(cookie);
    }

    public Page<UserResponseDto> getUser(int page, int size, String sortBy, boolean isAsc) {
        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> Users = userRepository.findAll(pageable);

        return Users.map(UserResponseDto::new);
    }

    @Transactional
    public UserResponseDto updateUser(Long UserId, UserUpdateRequestDto userUpdateRequestDto, HttpServletRequest request) {
        // 해당 유저가 DB에 존재하는지 확인
        User user = findUser(UserId);

        validateUserRequest(userUpdateRequestDto, false);

        //현재 패스워드가 맞는지
        if(!passwordEncoder.matches(userUpdateRequestDto.getPassword(), user.getPassword())) {
            throw new UserExceptions(NOT_MATCH_PASSWORD);//401
        }

        //변경할 비밀번호가 전과 같은지
        if(passwordEncoder.matches(userUpdateRequestDto.getConfirmPassword(), user.getPassword())) {
            throw new UserExceptions(SAME_BEFORE_PASSWORD);//401
        }

        //자신의 정보만 수정 가능
        String currentEmail = (String)request.getAttribute("email");

        user = userRepository.findByEmail(currentEmail).orElseThrow(() -> new ScheduleExceptions(NOT_FOUND_USER));

        if (!user.getId().equals(UserId)) {
            throw new ScheduleExceptions(HAS_NOT_PERMISSION);
        }

        String newPassword = passwordEncoder.encode(userUpdateRequestDto.getConfirmPassword());
        userUpdateRequestDto.setPassword(newPassword);

        user.update(userUpdateRequestDto);
        userRepository.save(user);
        return new UserResponseDto(user);
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

    private User validateUserRequest(UserRequestDto userRequestDto, boolean isSignup) {
        String username = null;
        String password = null;
        UserRole userRole = null;

        // 유저 이름
        if (userRequestDto.getUsername() != null) {
            username = userRequestDto.getUsername();
            if (username.length() > 4) {
                throw new UserExceptions(NAME_TOO_LONG);
            }
            if (username.isEmpty()) {
                throw new UserExceptions(USERNAME_REQUIRED);
            }
        }

        // 이메일
        String email = userRequestDto.getEmail();
        if (email.isEmpty()) {
            throw new UserExceptions(EMAIL_REQUIRED);
        }
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent() && isSignup) {
            throw new UserExceptions(DUPLICATE_EMAIL);
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            throw new UserExceptions(INVALID_EMAIL_FORMAT);
        }

        if (isSignup) {
            if (userRequestDto.getPassword() == null || userRequestDto.getPassword().trim().isEmpty()) {
                throw new UserExceptions(PASSWORD_REQUIRED);
            }
            // 패스워드 암호화
            password = passwordEncoder.encode(userRequestDto.getPassword());

            // 권한
            String role = userRequestDto.getRole();
            if (!(role.equals("ADMIN") || role.equals("USER"))) {
                throw new UserExceptions(ROLE_REQUIRED);
            }
            userRole = UserRole.valueOf(role);

            // User 객체 생성
            User user = new User(username, email, password, userRole);
            userRepository.save(user);
            return user; // 가입 시 User 객체 리턴
        }

        // 로그인 또는 업데이트 시 패스워드 체크
        if (userRequestDto.getPassword() != null && (userRequestDto.getPassword().isEmpty()
            || userRequestDto.getPassword().equals(" "))) {
            throw new UserExceptions(PASSWORD_REQUIRED);
        }
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserExceptions(NOT_FOUND_USER));
    }


    // private User validateSignupRequest(SignupRequestDto signupRequestDto) {
    //     //유저 이름
    //     String username = signupRequestDto.getUsername();
    //     if(username.length() > 4) {
    //         throw new UserExceptions(NAME_TOO_LONG);
    //     }
    //     if(username.isEmpty()) {
    //         throw new UserExceptions(USERNAME_REQUIRED);
    //     }
    //
    //     //이메일
    //     String email = signupRequestDto.getEmail();
    //     if(email.isEmpty()) {
    //         throw new UserExceptions(EMAIL_REQUIRED);
    //     }
    //     Optional<User> checkEmail = userRepository.findByEmail(email);
    //     if (checkEmail.isPresent()) {
    //         throw new UserExceptions(DUPLICATE_EMAIL);
    //     }
    //
    //     String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    //     if(!email.matches(emailRegex)) {
    //         throw new UserExceptions(INVALID_EMAIL_FORMAT);
    //     }
    //
    //     //패스워드
    //     if(signupRequestDto.getPassword().isEmpty() || signupRequestDto.getPassword().equals(" ")) {
    //         throw new UserExceptions(PASSWORD_REQUIRED);
    //     }
    //     String password = passwordEncoder.encode(signupRequestDto.getPassword());
    //
    //     //권한
    //     String role = signupRequestDto.getRole();
    //     if (!(role.equals("ADMIN") || role.equals("USER"))) {
    //         throw new UserExceptions(ROLE_REQUIRED);
    //     }
    //     UserRole userRole = UserRole.valueOf(role);
    //
    //     User user = new User(username, email, password, userRole);
    //     userRepository.save(user);
    //
    //     return user;
    // }
}
