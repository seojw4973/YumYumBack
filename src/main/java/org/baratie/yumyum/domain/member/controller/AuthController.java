package org.baratie.yumyum.domain.member.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.dto.LoginDto;
import org.baratie.yumyum.domain.member.dto.LoginResponseDto;
import org.baratie.yumyum.domain.member.dto.SignUpDto;
import org.baratie.yumyum.domain.member.dto.TokenDto;
import org.baratie.yumyum.domain.member.service.auth.AuthService;
import org.baratie.yumyum.domain.member.service.auth.JwtService;
import org.baratie.yumyum.domain.member.service.auth.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
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
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        LoginResponseDto loginResponseDtoDto = authService.login(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDtoDto);
    }

    /**
     * 토큰 재발급
     * @param request
     * @return
     */
    @PostMapping("/reissuse")
    public ResponseEntity<TokenDto> reissue(HttpServletRequest request, HttpServletResponse response) {
        TokenDto tokenDto = jwtService.reissueToken(request.getHeader("Authorization"));
        return ResponseEntity.status(HttpStatus.OK).body(tokenDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String rtk) {
        redisService.deleteValue(rtk);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
