package org.baratie.yumyum.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMemberDto {

    private String nickname;
    private String email;
    private String phoneNumber;
    private Boolean isDeleted;

}
