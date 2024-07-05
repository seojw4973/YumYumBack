package org.baratie.yumyum.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.Role;
import org.baratie.yumyum.domain.member.domain.SocialType;
import org.baratie.yumyum.domain.member.dto.*;
import org.baratie.yumyum.domain.member.exception.MemberNotFoundException;
import org.baratie.yumyum.domain.member.exception.PasswordNotEqualException;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 회원가입
     * @param signUpDto
     * @return
     */
    public String register(SignUpDto signUpDto){
        String response = null;
        try{
            if(!memberRepository.existsByNickname(signUpDto.getNickName())){
                String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
                Member member = new Member().builder()
                        .nickname(signUpDto.getNickName())
                        .email(signUpDto.getEmail())
                        .password(encodedPassword)
                        .phoneNumber(signUpDto.getPhoneNumber())
                        .imageUrl(signUpDto.getImageUrl())
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
     * 로그인
     * @param loginDto
     * @return
     */
    public LoginResponseDto login(LoginDto loginDto){
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            Authentication auth = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            Optional<Member> member = memberRepository.findByEmail(loginDto.getEmail());

            if(member.get().getSocialType() == SocialType.YUMYUM) {
                Long memberId = member.get().getId();
                String nickname = member.get().getNickname();
                String imageUrl = member.get().getImageUrl();

                String atk = jwtService.createToken(auth);
                String rtk = jwtService.createRtk(auth);

                return new LoginResponseDto(memberId, nickname, imageUrl, atk, rtk);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            throw new RuntimeException("로그인 실패", e); // 사용자 정의 예외 메시지와 함께 예외를 다시 던짐
        }
        return null;
    }

    /**
     * 내 정보 보기
     * @param memberId
     * @return
     */
    public MyInfoDto getMyInfo(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow();
        return MyInfoDto.fromEntity(member);
    }

    /**
     * 내 정보 수정
     * @param member 수정할 멤버
     * @param updateMemberDto 수정 요청한 정보
     */
    public void updateMember(Member member, UpdateMemberDto updateMemberDto) {
        if (updateMemberDto.getPassword() != updateMemberDto.getCheckPassword()) {
            new PasswordNotEqualException(ErrorCode.PASSWORD_NOT_EQUAL);
        }

        Member updateMember = member.updateInfo(updateMemberDto);

        memberRepository.save(updateMember);
    }

    /**
     * id 값에 해당하는 멤버 가져오기
     * @param memberId
     * @return 조회한 멤버
     */
    public Member getMember(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }

    /**
     * 탈퇴한 회원인지 확인
     */
    public void validationMemberId(Long memberId) {

        if (memberRepository.checkDeletedMember(memberId)) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

    }

    /**
     * 회원 존재 여부 확인
     * @param memberId
     * @return Member
     */
    public void exists(Long memberId){
        boolean existMember = memberRepository.existsById(memberId);
        if(!existMember){
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
