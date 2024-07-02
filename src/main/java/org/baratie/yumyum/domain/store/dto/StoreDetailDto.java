package org.baratie.yumyum.domain.store.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.hashtag.dto.HashtagDto;
import org.baratie.yumyum.domain.image.dto.ImageDto;
import org.baratie.yumyum.domain.menu.domain.Menu;
import org.baratie.yumyum.domain.menu.dto.MenuDto;
import org.baratie.yumyum.domain.store.domain.Store;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StoreDetailDto {

    private String name;
    private String address;
    private String hours;
    private String calls;
    private int views;
    private BigDecimal latitude;
    private BigDecimal longitude;

    private List<HashtagDto> hashtagList;
    private List<MenuDto> menuList;
    private List<ImageDto> imageList;

    private int reviewCount;
    private int favoriteCount;

    public static StoreDetailDto fromEntity(Store store, int reviewCount, int favoriteCount) {
        List<MenuDto> menuList = store.getMenuList().stream().map(menu -> MenuDto.builder()
                .name(menu.getName())
                .price(menu.getPrice())
                .build()).toList();

        List<HashtagDto> hashtagList = store.getHashtagList().stream().map(hashtag -> HashtagDto.builder()
                .content(hashtag.getContent())
                .build()).toList();

        List<ImageDto> imageList = store.getImageList().stream().map(image -> ImageDto.builder()
                .imageUrl(image.getImageUrl())
                .build()).toList();

        return StoreDetailDto.builder()
                .name(store.getName())
                .address(store.getAddress())
                .hours(store.getHours())
                .views(store.getViews())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .reviewCount(reviewCount)
                .favoriteCount(favoriteCount)
                .hashtagList(hashtagList)
                .menuList(menuList)
                .imageList(imageList)
                .build();
    }

}
