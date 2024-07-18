package org.baratie.yumyum.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.Role;
import org.baratie.yumyum.domain.member.domain.SocialType;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignUpDto {

    @NotBlank @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String nickname;

    public Member toEntity(String password, String imageUrl){
        return Member.builder()
                .email(this.email)
                .password(password)
                .phoneNumber(this.phoneNumber)
                .imageUrl(imageUrl)
                .nickname(this.nickname)
                .role(Role.USER)
                .socialType(SocialType.YUMYUM)
                .build();
    }
}
