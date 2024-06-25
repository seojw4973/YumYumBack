package org.baratie.yumyum.domain.menu.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuDto {

    private String name;
    private int price;
}
