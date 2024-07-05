package org.baratie.yumyum.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberDto {

    private String profileImage;
    private String nickname;
    private String phoneNumber;
    private String password;
    private String checkPassword;

}
