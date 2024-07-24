package org.baratie.yumyum.domain.member.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TokenDto {
    private String atk;
    private String rtk;

    public static TokenDto fromToken(String atk, String rtk) {
        return TokenDto.builder()
                .atk(atk)
                .rtk(rtk)
                .build();
    }
}
