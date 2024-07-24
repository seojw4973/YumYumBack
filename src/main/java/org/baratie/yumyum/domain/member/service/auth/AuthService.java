package org.baratie.yumyum.domain.member.service.auth;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.Role;
import org.baratie.yumyum.domain.member.domain.SocialType;
import org.baratie.yumyum.domain.member.dto.*;
import org.baratie.yumyum.domain.member.exception.DeletedMemberException;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.utils.file.service.ImageService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final ImageService imageService;
    private final RedisService redisService;

    /**
     * 회원가입
     * @param signUpDto
     * @return
     */
    public void register(SignUpDto signUpDto, MultipartFile file) throws IOException {
        memberService.nicknameDuplicateCheck(signUpDto.getNickname());
        memberService.emailDuplicateCheck(signUpDto.getEmail());
        String password = passwordEncoder.encode(signUpDto.getPassword());
        String profileUrl = null;
        if(file != null && !file.isEmpty()){
            profileUrl = imageService.profileImageUpload(file);
        }
        Member member = signUpDto.toEntity(password, profileUrl);
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
            Member member = memberService.getMemberByEmail(loginDto.getEmail());

            if(member.isDeleted()){
                System.out.println("존재하지 않는 회원입니다.");
                throw new DeletedMemberException(ErrorCode.MEMBER_IS_DELETED);
            }

            if(member.getSocialType() == SocialType.YUMYUM) {
                String atk = jwtService.createAtk(auth);
                String rtk = jwtService.createRtk(auth);

                UserDataDto userDataDto = UserDataDto.fromEntity(member);

                TokenDto tokenDto = TokenDto.fromToken(atk, rtk);

                return new LoginResponseDto(userDataDto, tokenDto);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            throw new RuntimeException("로그인 실패", e); // 사용자 정의 예외 메시지와 함께 예외를 다시 던짐
        }
        return null;
    }

    public void logout(String email){
        if(redisService.getValue(email)!= null) {
            redisService.deleteValue(email);
        }
    }
}
