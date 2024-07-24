package org.baratie.yumyum.domain.member.dto;

import lombok.*;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.Role;

@Getter
@AllArgsConstructor
@ToString
public class LoginResponseDto {

    private TokenDto tokenDto;
    private UserDataDto userDataDto;


    public LoginResponseDto(UserDataDto userDataDto, TokenDto tokenDto) {
        this.userDataDto = userDataDto;
        this.tokenDto = tokenDto;
    }


}
