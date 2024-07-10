package org.baratie.yumyum.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.SocialType;
import org.baratie.yumyum.domain.member.dto.*;
import org.baratie.yumyum.domain.member.exception.MemberNotFoundException;
import org.baratie.yumyum.domain.member.exception.NicknameAlreadyUsing;
import org.baratie.yumyum.domain.member.exception.PasswordNotEqualException;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * 회원가입
     * @param signUpDto
     * @return
     */
    public void register(SignUpDto signUpDto){
        nicknameDuplicateCheck(signUpDto.getNickName());
        String password = passwordEncoder.encode(signUpDto.getPassword());
        Member member = signUpDto.toEntity(password);
        memberRepository.save(member);
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
            Member member = getMemberByEmail(loginDto.getEmail());

            if(member.getSocialType() == SocialType.YUMYUM) {
                Long memberId = member.getId();
                String nickname = member.getNickname();
                String imageUrl = member.getImageUrl();
                String phoneNumber = member.getPhoneNumber();

                String atk = jwtService.createToken(auth);
                String rtk = jwtService.createRtk(auth);

                return new LoginResponseDto(memberId, nickname, imageUrl, phoneNumber, atk, rtk);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            throw new RuntimeException("로그인 실패", e); // 사용자 정의 예외 메시지와 함께 예외를 다시 던짐
        }
        return null;
    }

    /**
     * 관리자페이지 회원 조회
     */
    public Page<SimpleMemberDto> getSimpleMemberInfo(Pageable pageable) {
        return memberRepository.getSimpleMemberInfo(pageable);
    }

    /**
     * 내 정보 보기
     * @param memberId
     * @return
     */
    public MyInfoDto getMyInfo(Long memberId){
        Member member = getMember(memberId);
        return MyInfoDto.fromEntity(member);
    }

    /**
     * 내 정보 수정
     * @param member 수정할 멤버
     * @param updateMemberDto 수정 요청한 정보
     */
    public void updateMember(Member member, UpdateMemberDto updateMemberDto) {

        nicknameDuplicateCheck(updateMemberDto.getNickname());
        passwordCheck(updateMemberDto);

        Member updateMember = member.updateInfo(updateMemberDto);

        memberRepository.save(updateMember);
    }

    /**
     * 닉네임 중복 체크
     * @param nickname 변경할 닉네임
     */
    private void nicknameDuplicateCheck(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new NicknameAlreadyUsing(ErrorCode.EXIST_MEMBER_NICKNAME);
        }
    }

    /**
     * 비밀번호 동일 체크
     */
    private void passwordCheck(UpdateMemberDto updateMemberDto) {
        if (!updateMemberDto.getPassword().equals(updateMemberDto.getCheckPassword())) {
            throw new PasswordNotEqualException(ErrorCode.PASSWORD_NOT_EQUAL);
        }

        String encodingPassword = passwordEncoder.encode(updateMemberDto.getPassword());

        updateMemberDto.setPassword(encodingPassword);
    }

    /**
     * id 값에 해당하는 멤버 가져오기
     */
//    public Member getMember(Long memberId){
//        exists(memberId);
//        validationMemberId(memberId);
//        return memberRepository.findById(memberId).orElseThrow(
//                () -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
//        );
//    }

    /**
     * email 값에 해당하는 멤버 가져오기
     */
    public Member getMemberByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
        );
    }

    public void deleteMember(Long memberId) {
        Member member = getMember(memberId);
        Member deletedMember = member.deleteMember(memberId);

        memberRepository.save(deletedMember);
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
     */
    public void exists(Long memberId){
        boolean existMember = memberRepository.existsById(memberId);
        if(!existMember){
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
    }

    public Member getMember(Long memberId){
        Member member = memberRepository.findByIdNotDeleted(memberId);
        if(member == null){
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return member;
    }
}
