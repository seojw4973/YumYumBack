package org.baratie.yumyum.domain.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.dto.LoginDto;
import org.baratie.yumyum.domain.member.dto.LoginResponseDto;
import org.baratie.yumyum.domain.member.dto.MemberDto;
import org.baratie.yumyum.domain.member.dto.TokenDto;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @param memberDTO
     * @return response
     */
    @PostMapping
    public ResponseEntity<String> registerMember(@RequestBody MemberDto memberDTO) {
        String response = memberService.register(memberDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * 로그인
     * @param loginDto
     * @return TokenDto
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        System.out.println("login.....");
        LoginResponseDto loginResponseDtoDto = memberService.login(loginDto);
        System.out.println("Token: " + loginResponseDtoDto);
        return ResponseEntity.ok().body(loginResponseDtoDto);
    }

    /**
     * 내 정보 보기
     * @param memberId
     * @return
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long memberId) {
        System.out.println("getMember...");
        MemberDto memberDto = memberService.getMyInfo(memberId);
        System.out.println("memberDto: " + memberDto);
        return ResponseEntity.ok().body(memberDto);
    }

//    @GetMapping("/oauth2")
//    public void oauth2login(HttpServletResponse response) throws IOException {
//       String redirectUri = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=5Xxn8DYEG64BCrrG25xx&redirect_uri=http://localhost:3000/callbackr&state=test";
//       response.sendRedirect(redirectUri);
//
//    }


}
