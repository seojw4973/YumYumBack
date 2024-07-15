package org.baratie.yumyum.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.dto.*;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    /**
     * 내 정보 보기
     * @param customUserDetails 로그인한 유저 정보
     * @return 로그인한 유저의 상세 정보
     */
    @GetMapping
    public ResponseEntity<MyInfoDto> getMember(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long memberId = customUserDetails.getId();
        MyInfoDto myInfoDto = memberService.getMyInfo(memberId);
        return ResponseEntity.ok().body(myInfoDto);
    }

    /**
     * 내 정보 수정
     */
    @PatchMapping
    public ResponseEntity<Void> updateMember(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @Valid @RequestPart UpdateMemberDto updateMemberDto,
                                             @RequestPart(required = false) MultipartFile file) throws Exception {
        Member member = memberService.getMember(customUserDetails.getId());

        memberService.updateMember(member, updateMemberDto, file);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
