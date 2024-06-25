package org.baratie.yumyum.domain.store.dto;

import lombok.*;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.menu.domain.Menu;

import java.util.List;

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

    private List<Hashtag> hashtagList;
    private List<Menu> menuList;

    private int reviewCount;
    private int favoriteCount;

}
