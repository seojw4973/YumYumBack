package org.baratie.yumyum.domain.member.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberDto {

    private String nickname;
    private String phoneNumber;

    @Setter
    private String password;

}
