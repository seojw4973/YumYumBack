package org.baratie.yumyum.domain.favorite.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FavoriteDto {

    @NotBlank
    private Long storeId;

    @NotBlank
    private Boolean favoriteStatus;
}
