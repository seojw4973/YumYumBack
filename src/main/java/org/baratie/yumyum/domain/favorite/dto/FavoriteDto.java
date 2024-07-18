package org.baratie.yumyum.domain.favorite.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FavoriteDto {

    @NotBlank
    private Long storeId;

    @NotNull
    private Boolean favoriteStatus;
}
