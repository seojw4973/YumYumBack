package org.baratie.yumyum.domain.member.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.dto.*;
import org.baratie.yumyum.domain.member.service.auth.AuthService;
import org.baratie.yumyum.domain.member.service.auth.JwtService;
import org.baratie.yumyum.domain.member.service.auth.RedisService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@CrossOrigin(exposedHeaders = "*")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final RedisService redisService;

    /**
     * 회원가입
     * @param signUpDto 회원가입 정보
     * @return response 회원가입 완료 여부
     */
    @PostMapping
    public ResponseEntity<String> registerMember(@Valid @RequestPart SignUpDto signUpDto,
                                                 @RequestPart(required = false) MultipartFile file) throws IOException {
        authService.register(signUpDto, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 로그인
     * @param loginDto 이메일과 비밀먼호
     * @return LoginResponseDto 로그인한 유저의 정보 및 jwt 리턴
     */
    @PostMapping("/login")
    public ResponseEntity<UserDataDto> login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        LoginResponseDto loginResponseDto = authService.login(loginDto);
        TokenDto tokenDto = loginResponseDto.getTokenDto();
        UserDataDto userDataDto = loginResponseDto.getUserDataDto();

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAtk());

        Cookie rtkCookie = new Cookie("rtk", tokenDto.getRtk());
        rtkCookie.setHttpOnly(true);
        rtkCookie.setSecure(true);
        rtkCookie.setPath("/");
        rtkCookie.setMaxAge(604800);
        response.addCookie(rtkCookie);

        return ResponseEntity.status(HttpStatus.OK).body(userDataDto);
    }

    /**
     * 토큰 재발급
     * @param request cookie에 rtk 담아서 재발급 요청 보냄
     * @return
     */
    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("rtk")) {
                    token = cookie.getValue();
                }
            }
        }
        TokenDto tokenDto = jwtService.reissueToken(token);
        Cookie rtkCookie = new Cookie("rtk", tokenDto.getRtk());
        rtkCookie.setHttpOnly(true);
        rtkCookie.setSecure(true);
        rtkCookie.setPath("/");
        rtkCookie.setMaxAge(604800);
        response.addCookie(rtkCookie);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAtk());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 로그아웃
     * @param customUserDetails 로그인한 유저 정보
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletRequest request, HttpServletResponse response) {
        String email = customUserDetails.getUsername();
        authService.logout(email);

        Cookie rtkCookie = new Cookie("rtk", "");
        rtkCookie.setPath("/");
        rtkCookie.setMaxAge(0);
        response.addCookie(rtkCookie);

        response.addHeader(HttpHeaders.AUTHORIZATION, "");

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
