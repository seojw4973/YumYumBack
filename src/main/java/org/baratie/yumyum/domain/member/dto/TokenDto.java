package org.baratie.yumyum.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDto {
    private final String atk;
    private final String rtk;
}
