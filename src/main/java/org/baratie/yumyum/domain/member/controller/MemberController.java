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
     *
     * @param memberDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<String> registerMember(@RequestBody MemberDto memberDTO) {
        String response = memberService.register(memberDTO);
        return ResponseEntity.ok(response);
    }

    /**
     *
     * @param loginDto
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        System.out.println("login.....");
        TokenDto tokenDto = memberService.login(loginDto);
        System.out.println("Token: " + tokenDto);
        return ResponseEntity.ok().body(tokenDto);
    }

    /**
     *
     * @param member_id
     * @return
     */
    @GetMapping("/{member_id}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long member_id) {
        System.out.println("getMember...");
        MemberDto memberDto = memberService.getMyInfo(member_id);
        System.out.println("memberDto: " + memberDto);
        return ResponseEntity.ok().body(memberDto);
    }


}
