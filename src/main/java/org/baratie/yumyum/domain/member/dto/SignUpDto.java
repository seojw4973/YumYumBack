package org.baratie.yumyum.domain.member.dto;

import lombok.*;
import org.baratie.yumyum.domain.member.domain.Member;

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
    private String imageUrl;

    public Member toEntity(String password){
        return Member.builder()
                .email(this.email)
                .password(password)
                .phoneNumber(this.phoneNumber)
                .imageUrl(this.imageUrl)
                .nickname(this.nickName)
                .build();
    }
}
