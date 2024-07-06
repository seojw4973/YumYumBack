package org.baratie.yumyum.domain.member.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginResponseDto {
    Long memberId;
    String nickName;
    String imageUrl;
    String atk;
    String rtk;

}
