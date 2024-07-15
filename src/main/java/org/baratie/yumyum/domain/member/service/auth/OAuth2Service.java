package org.baratie.yumyum.domain.member.service.auth;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.Role;
import org.baratie.yumyum.domain.member.domain.SocialType;
import org.baratie.yumyum.domain.member.oauth.GoogleUserInfo;
import org.baratie.yumyum.domain.member.oauth.KakaoUserInfo;
import org.baratie.yumyum.domain.member.oauth.NaverUserInfo;
import org.baratie.yumyum.domain.member.oauth.OAuth2UserInfo;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2Service extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String nickname = null;
        SocialType socialType = null;
        OAuth2UserInfo oAuth2UserInfo = null;
        String email = null;

        String provider = userRequest.getClientRegistration().getRegistrationId();

        if(provider.equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo( (Map)oAuth2User.getAttributes().get("response"));
            socialType = SocialType.NAVER;
            nickname = "Naver" + oAuth2UserInfo.getName();
            email = oAuth2UserInfo.getEmail();
            if(email == null){
                email = oAuth2UserInfo.getName() + "@naver.com";
            }
        } else if(provider.equals("kakao")){
            System.out.println("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo( (Map)oAuth2User.getAttributes());
            socialType = SocialType.KAKAO;
            nickname = "Kakao" + oAuth2UserInfo.getName();
            email = oAuth2UserInfo.getEmail();
            if(email == null){
                email = oAuth2User.getName() + "@kakao.com";
            }
        } else if(provider.equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo( oAuth2User.getAttributes() );
            nickname = "Google" + oAuth2UserInfo.getName();
        }

        String phoneNumber = oAuth2UserInfo.getPhone();

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = null;

        if(optionalMember.isEmpty()){
            member = new Member().builder()
                    .email(email)
                    .nickname(nickname)
                    .role(Role.USER)
                    .imageUrl("22")
                    .phoneNumber(phoneNumber)
                    .socialType(socialType)
                    .build();
            memberRepository.save(member);
        }else{
            member = optionalMember.get();
        }
        return new CustomUserDetails(member, oAuth2User.getAttributes());
    }


}
