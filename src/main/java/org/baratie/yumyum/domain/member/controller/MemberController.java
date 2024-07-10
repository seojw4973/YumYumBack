package org.baratie.yumyum.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.Role;
import org.baratie.yumyum.domain.member.dto.*;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원가입
     *
     * @param signUpDto 회원가입 정보
     * @return response 회원가입 완료 여부
     */
    @PostMapping
    public ResponseEntity<String> registerMember(@RequestBody SignUpDto signUpDto) {
        memberService.register(signUpDto);
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

    /**
     * 내 정보 보기
     *
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
    public ResponseEntity<Void> updateMember(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody UpdateMemberDto updateMemberDto) {
        Member member = memberService.getMember(customUserDetails.getId());

        memberService.updateMember(member, updateMemberDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 관리자 페이지 회원 전체 조회
     */
    @GetMapping("/admin/member")
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

        return ResponseEntity.ok().build();
    }


//    @GetMapping("/oauth2")
//    public void oauth2login(HttpServletResponse response) throws IOException {
//       String redirectUri = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=5Xxn8DYEG64BCrrG25xx&redirect_uri=http://localhost:3000/callbackr&state=test";
//       response.sendRedirect(redirectUri);
//
//    }


}
