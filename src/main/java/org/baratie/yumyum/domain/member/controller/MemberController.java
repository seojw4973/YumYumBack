package org.baratie.yumyum.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.dto.LoginDto;
import org.baratie.yumyum.domain.member.dto.MemberDto;
import org.baratie.yumyum.domain.member.dto.TokenDto;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        System.out.println("login.....");
        TokenDto tokenDto = memberService.login(loginDto);
        System.out.println("Token: " + tokenDto);
        return ResponseEntity.ok().body(tokenDto);
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


}
