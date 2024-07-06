package org.baratie.yumyum.domain.member.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberDto {

    private String profileImage;
    private String nickname;
    private String phoneNumber;
    @Setter private String password;
    private String checkPassword;

}
