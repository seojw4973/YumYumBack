package org.baratie.yumyum.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.Role;
import org.baratie.yumyum.domain.member.domain.SocialType;
import org.baratie.yumyum.domain.member.dto.LoginDto;
import org.baratie.yumyum.domain.member.dto.MemberDto;
import org.baratie.yumyum.domain.member.dto.TokenDto;
import org.baratie.yumyum.domain.member.exception.MemberNotFoundException;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     *
     * @param memberDTO
     * @return
     */
    public String register(MemberDto memberDTO){
        String response = null;
        try{
            if(!memberRepository.existsByNickname(memberDTO.getNickName())){
                String encodedPassword = passwordEncoder.encode(memberDTO.getPassword());
                Member member = new Member().builder()
                        .nickname(memberDTO.getNickName())
                        .email(memberDTO.getEmail())
                        .password(encodedPassword)
                        .phoneNumber(memberDTO.getPhoneNumber())
                        .imageUrl(memberDTO.getImageUrl())
                        .role(Role.USER)
                        .isDeleted(false)
                        .socialType(SocialType.YUMYUM).build();

                memberRepository.save(member);
                response = "회원가입에 성공하였습니다.";
            }else{
                response = "닉네임이 중복됩니다.";
            }
        } catch (Exception e) {
            response = "회원가입에 실패하였습니다.";
        }
        return response;
    }

    /**
     *
     * @param loginDto
     * @return
     */
    public TokenDto login(LoginDto loginDto){
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
            System.out.println("authenticationToken : " + authenticationToken);

            Authentication auth = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            System.out.println("auth :" + auth);

            TokenDto tokenDto = jwtService.createToken(auth);
            return tokenDto;
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            throw new RuntimeException("로그인 실패", e); // 사용자 정의 예외 메시지와 함께 예외를 다시 던짐
        }
    }

    /**
     *
     * @param memberId
     * @return
     */
    public MemberDto getMyInfo(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow();

        MemberDto memberDto = new MemberDto().builder()
                .email(member.getEmail())
                .nickName(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .imageUrl(member.getImageUrl()).build();

        return memberDto;
    }

    /**
     * 회원 존재 여부 확인
     * @param memberId
     * @return Member
     */
    public Member validationMemberId(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }

}
