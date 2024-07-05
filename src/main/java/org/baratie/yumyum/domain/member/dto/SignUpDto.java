package org.baratie.yumyum.domain.member.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignUpDto {

    private String email;
    private String password;
    private String phoneNumber;
    private String nickName;
    private String imageUrl;
}
