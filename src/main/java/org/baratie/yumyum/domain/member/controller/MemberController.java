package org.baratie.yumyum.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.dto.*;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
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
     * @param signUpDto 회원가입 정보
     * @return response 회원가입 완료 여부
     */
    @PostMapping
    public ResponseEntity<String> registerMember(@RequestBody SignUpDto signUpDto) {
        String response = memberService.register(signUpDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 로그인
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
     * @param memberId 로그인한 유저 id
     * @return 로그인한 유저의 상세 정보
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<MyInfoDto> getMember(@PathVariable Long memberId) {
        MyInfoDto myInfoDto = memberService.getMyInfo(memberId);
        return ResponseEntity.ok().body(myInfoDto);
    }





//    @GetMapping("/oauth2")
//    public void oauth2login(HttpServletResponse response) throws IOException {
//       String redirectUri = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=5Xxn8DYEG64BCrrG25xx&redirect_uri=http://localhost:3000/callbackr&state=test";
//       response.sendRedirect(redirectUri);
//
//    }


}
