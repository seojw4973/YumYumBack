package org.baratie.yumyum.domain.member.dto;

import lombok.*;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.Role;
import org.baratie.yumyum.domain.member.domain.SocialType;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignUpDto {

    private String email;
    private String password;
    private String phoneNumber;
    private String nickName;

    public Member toEntity(String password, String imageUrl){
        return Member.builder()
                .email(this.email)
                .password(password)
                .phoneNumber(this.phoneNumber)
                .imageUrl(imageUrl)
                .nickname(this.nickName)
                .role(Role.USER)
                .socialType(SocialType.YUMYUM)
                .build();
    }
}
