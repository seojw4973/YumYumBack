package org.baratie.yumyum.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.Role;
import org.baratie.yumyum.domain.member.domain.SocialType;
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
        SocialType socialType = null;
        OAuth2UserInfo oAuth2UserInfo = null;

        String provider = userRequest.getClientRegistration().getRegistrationId();

        if(provider.equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo( (Map)oAuth2User.getAttributes().get("response"));
            socialType = SocialType.NAVER;
        }

        String email = oAuth2UserInfo.getEmail();
        String nickname = oAuth2UserInfo.getName();

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = null;

        if(optionalMember.isEmpty()){
            member = new Member().builder()
                    .email(email)
                    .nickname(nickname)
                    .role(Role.USER)
                    .socialType(socialType)
                    .build();
        }else{
            member = optionalMember.get();
        }
        return new CustomUserDetails(member, oAuth2User.getAttributes());
    }


}
