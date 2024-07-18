package org.baratie.yumyum.domain.member.dto;

import lombok.*;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.Role;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginResponseDto {
    Long memberId;
    String nickname;
    String imageUrl;
    String phoneNumber;
    Role role;
    String atk;
    String rtk;
}
