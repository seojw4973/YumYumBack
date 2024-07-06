package org.baratie.yumyum.domain.member.dto;

import lombok.*;
import org.baratie.yumyum.domain.member.domain.Member;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginResponseDto {
    Long memberId;
    String nickName;
    String imageUrl;
    String phoneNumber;
    String atk;
    String rtk;

//    public LoginResponseDto tranceDto(Member member, String atk, String rtk) {
//        return LoginResponseDto.builder()
//                .memberId(member.getId())
//                .nickName(member.getNickname())
//                .imageUrl(member.getImageUrl())
//                .atk(atk)
//                .rtk(rtk)
//                .build();
//
//    }
}
