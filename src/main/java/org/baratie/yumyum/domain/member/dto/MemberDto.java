package org.baratie.yumyum.domain.member.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDto {

    private String email;
    private String password;
    private String phoneNumber;
    private String nickName;
    private String imageUrl;

    public MemberDto(String email, String phoneNumber, String nickName, String imageUrl) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
        this.imageUrl = imageUrl;
    }

}