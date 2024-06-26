package org.baratie.yumyum.domain.menu.dto;

import lombok.*;
import org.baratie.yumyum.domain.menu.domain.Menu;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuDto {

    private String name;
    private int price;

    public Menu toEntity(){
        return Menu.builder()
                .name(this.name)
                .price(this.price)
                .build();
    }
}
