package org.baratie.yumyum.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.dto.MemberDTO;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<String> registerMember(MemberDTO memberDTO) {
        String response = memberService.register(memberDTO);
        return ResponseEntity.ok().body(response);
    }


}
