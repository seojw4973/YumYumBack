package org.baratie.yumyum.domain.member.dto;

import lombok.*;
import org.baratie.yumyum.domain.member.domain.Member;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MyInfoDto {

    private Long memberId;
    private String email;
    private String phoneNumber;
    private String nickname;
    private String imageUrl;

    public static MyInfoDto fromEntity(Member member) {
        return MyInfoDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .imageUrl(member.getImageUrl()).build();
    }

}