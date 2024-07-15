package org.baratie.yumyum.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.dto.*;
import org.baratie.yumyum.domain.member.exception.MemberNotFoundException;
import org.baratie.yumyum.domain.member.exception.NicknameAlreadyUsing;
import org.baratie.yumyum.domain.member.exception.PasswordNotEqualException;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.utils.file.service.ImageService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;


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
    public void updateMember(Member member, UpdateMemberDto updateMemberDto, MultipartFile file) throws IOException {
        nicknameDuplicateCheck(updateMemberDto.getNickname());
        passwordCheck(updateMemberDto);
        String profileUrl;
        if(file != null && !file.isEmpty()){
            if(member.getImageUrl() != null) {
                imageService.targetFileDelete(member);
            }
            profileUrl = imageService.profileImageUpload(file);
        }else {
            imageService.targetFileDelete(member);
            profileUrl = null;
        }

        Member updateMember = member.updateInfo(updateMemberDto, profileUrl);

        memberRepository.save(updateMember);
    }

    /**
     * 닉네임 중복 체크
     * @param nickname 변경할 닉네임
     */
    public void nicknameDuplicateCheck(String nickname) {
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
