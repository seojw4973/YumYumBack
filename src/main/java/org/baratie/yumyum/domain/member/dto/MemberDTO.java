package org.baratie.yumyum.domain.member.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberDTO {

    private String email;
    private String password;
    private String phoneNumber;
    private String nickName;
    private String imageUrl;

}
