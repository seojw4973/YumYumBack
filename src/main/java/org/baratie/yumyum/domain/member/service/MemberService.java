package org.baratie.yumyum.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.Role;
import org.baratie.yumyum.domain.member.domain.SocialType;
import org.baratie.yumyum.domain.member.dto.LoginDto;
import org.baratie.yumyum.domain.member.dto.MemberDto;
import org.baratie.yumyum.domain.member.dto.TokenDto;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.springframework.security.authentication.AuthenticationManager;
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

    public TokenDto login(LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        System.out.println("authenticationToken : " + authenticationToken);

        Authentication auth = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        System.out.println("auth :" + auth);
        TokenDto tokenDto = jwtService.createToken(auth);

        return tokenDto;
    }

}
