package org.baratie.yumyum.domain.member.dto;

import lombok.*;
import org.baratie.yumyum.domain.member.domain.Member;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MyInfoDto {

    private String email;
    private String phoneNumber;
    private String nickName;
    private String imageUrl;

    public static MyInfoDto fromEntity(Member member) {
        return MyInfoDto.builder()
                .email(member.getEmail())
                .nickName(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .imageUrl(member.getImageUrl()).build();
    }

}