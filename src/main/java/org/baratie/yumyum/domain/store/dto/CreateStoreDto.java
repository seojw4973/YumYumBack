package org.baratie.yumyum.domain.store.dto;

import com.google.maps.errors.ApiException;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baratie.yumyum.domain.category.domain.Category;
import org.baratie.yumyum.domain.category.dto.CategoryDto;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.hashtag.dto.HashtagDto;
import org.baratie.yumyum.domain.menu.domain.Menu;
import org.baratie.yumyum.domain.menu.dto.MenuDto;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.service.GeoUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateStoreDto {

    private final GeoUtils geoUtils = new GeoUtils();

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    private String calls;

    private String hours;
    private boolean isClosed;
    private BigDecimal latitude;
    private BigDecimal longitude;

    List<HashtagDto> hashtagList;
    List<MenuDto> menuList;
    List<CategoryDto> categoryList;

    public Store toEntity(BigDecimal latitude, BigDecimal longitude) throws IOException, InterruptedException, ApiException {
        List<Hashtag> hashtagList = (this.hashtagList != null)
                ? this.hashtagList.stream().map(HashtagDto::toEntity).collect(Collectors.toList())
                : new ArrayList<>();

        List<Menu> menuList = (this.menuList != null)
                ? this.menuList.stream().map(MenuDto::toEntity).collect(Collectors.toList())
                : new ArrayList<>();

        List<Category> categoryList = (this.categoryList != null)
                ? this.categoryList.stream().map(CategoryDto::toEntity).collect(Collectors.toList())
                : new ArrayList<>();

        return Store.builder()
                .name(this.name)
                .address(this.address)
                .call(this.calls)
                .hours(this.hours)
                .isClosed(this.isClosed)
                .latitude(latitude)
                .longitude(longitude)
                .hashtagList(hashtagList)
                .menuList(menuList)
                .categoryList(categoryList)
                .build();
    }

}
