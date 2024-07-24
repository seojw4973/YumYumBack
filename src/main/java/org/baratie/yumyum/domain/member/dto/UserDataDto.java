package org.baratie.yumyum.domain.member.dto;

import lombok.*;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.Role;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDataDto {
    Long memberId;
    String nickname;
    String imageUrl;
    String phoneNumber;
    Role role;

    public static UserDataDto fromEntity(Member member){
        return UserDataDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .imageUrl(member.getImageUrl())
                .phoneNumber(member.getPhoneNumber())
                .role(member.getRole())
                .build();
    }
}
