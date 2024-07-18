package org.baratie.yumyum.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.baratie.yumyum.domain.category.dto.CategoryDto;
import org.baratie.yumyum.domain.hashtag.dto.HashtagDto;
import org.baratie.yumyum.domain.menu.dto.MenuDto;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class UpdateStoreDto {

    @NotBlank
    private String name;
    @NotBlank
    private String call;
    @NotBlank
    private String address;
    @NotBlank
    private String hours;

    private BigDecimal latitude;
    private BigDecimal longitude;

    List<HashtagDto> hashtagList;
    List<MenuDto> menuList;
    List<CategoryDto> categoryList;

    public UpdateStoreDto tranceDto(BigDecimal latitude, BigDecimal longitude, List<HashtagDto> hashtagList, List<MenuDto> menuList, List<CategoryDto> categoryList) {
        UpdateStoreDto updateStoreDto = UpdateStoreDto.builder()
                .name(name)
                .call(call)
                .address(address)
                .hours(hours)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        updateStoreDto.hashtagList = hashtagList;
        updateStoreDto.menuList = menuList;
        updateStoreDto.categoryList = categoryList;

        return updateStoreDto;
    }

    @Builder
    public UpdateStoreDto(String name, String call, String address, String hours, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.call = call;
        this.address = address;
        this.hours = hours;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
