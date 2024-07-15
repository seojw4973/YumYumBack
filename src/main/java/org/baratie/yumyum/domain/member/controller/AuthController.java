package org.baratie.yumyum.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.dto.LoginDto;
import org.baratie.yumyum.domain.member.dto.LoginResponseDto;
import org.baratie.yumyum.domain.member.dto.SignUpDto;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class AuthController {
    private final MemberService memberService;

    /**
     * 회원가입
     *
     * @param signUpDto 회원가입 정보
     * @return response 회원가입 완료 여부
     */
    @PostMapping
    public ResponseEntity<String> registerMember(@RequestPart SignUpDto signUpDto,
                                                 @RequestPart(required = false) MultipartFile file) throws IOException {
        memberService.register(signUpDto, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 로그인
     *
     * @param loginDto 이메일과 비밀먼호
     * @return LoginResponseDto 로그인한 유저의 정보 및 jwt 리턴
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        LoginResponseDto loginResponseDtoDto = memberService.login(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDtoDto);
    }

}
