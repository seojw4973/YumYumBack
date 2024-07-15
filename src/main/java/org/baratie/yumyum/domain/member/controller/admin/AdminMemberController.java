package org.baratie.yumyum.domain.member.controller.admin;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Role;
import org.baratie.yumyum.domain.member.dto.SimpleMemberDto;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminMemberController {

    private final MemberService memberService;

    /**
     * 관리자 페이지 회원 전체 조회
     */
    @GetMapping("/member")
    public ResponseEntity<Page<SimpleMemberDto>> getAllMember(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PageableDefault(size = 5) Pageable pageable) {
        customUserDetails.getAuthorities().equals(Role.ADMIN);


        Page<SimpleMemberDto> simpleMemberDto = memberService.getSimpleMemberInfo(pageable);

        return new ResponseEntity<>(simpleMemberDto, HttpStatus.OK);
    }

    /**
     * 회원 탈퇴
     * @param memberId 탈퇴시킬 회원
     */
    @DeleteMapping("/{memberId}/admin")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);

        return ResponseEntity.noContent().build();
    }

}
