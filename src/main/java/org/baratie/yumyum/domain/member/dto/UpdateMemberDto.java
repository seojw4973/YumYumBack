package org.baratie.yumyum.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberDto {

    private String profileImage;
    private String nickname;
    private String phoneNumber;

    @NotBlank @Setter
    private String password;
    @NotBlank
    private String checkPassword;

    public UpdateMemberDto tranceDto(UpdateMemberDto updateMemberDto, String url) {
        this.profileImage = url;
        this.nickname = updateMemberDto.getNickname();
        this.phoneNumber = updateMemberDto.getPhoneNumber();
        this.password = updateMemberDto.getPassword();
        this.checkPassword = updateMemberDto.getCheckPassword();
        return this;
    }

}
