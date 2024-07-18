package org.baratie.yumyum.domain.menu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.baratie.yumyum.domain.menu.domain.Menu;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuDto {

    @NotBlank
    private String name;

    @NotBlank
    private int price;

    public Menu toEntity(){
        return Menu.builder()
                .name(this.name)
                .price(this.price)
                .build();
    }
}
