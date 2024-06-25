package org.baratie.yumyum.domain.store.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.hashtag.dto.HashtagDto;
import org.baratie.yumyum.domain.menu.domain.Menu;
import org.baratie.yumyum.domain.menu.dto.MenuDto;
import org.baratie.yumyum.domain.store.domain.Store;

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

    private List<HashtagDto> hashtagList;
    private List<MenuDto> menuList;

    private int reviewCount;
    private int favoriteCount;

    public static StoreDetailDto fromEntity(Store store, int reviewCount, int favoriteCount) {
        List<MenuDto> menuList = store.getMenuList().stream().map(menu -> MenuDto.builder()
                .name(menu.getName())
                .price(menu.getPrice())
                .build()).collect(Collectors.toList());

        List<HashtagDto> hashtagList = store.getHashtagList().stream().map(hashtag -> HashtagDto.builder()
                .content(hashtag.getContent())
                .build()).toList();

        return StoreDetailDto.builder()
        .name(store.getName())
                .address(store.getAddress())
                .hours(store.getHours())
                .views(store.getViews())
                .reviewCount(reviewCount)
                .favoriteCount(favoriteCount)
                .hashtagList(hashtagList)
                .menuList(menuList)
                .build();
    }

}
