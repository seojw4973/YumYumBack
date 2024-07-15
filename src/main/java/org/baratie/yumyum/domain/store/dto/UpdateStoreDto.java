package org.baratie.yumyum.domain.store.dto;

import lombok.*;
import org.baratie.yumyum.domain.hashtag.dto.HashtagDto;
import org.baratie.yumyum.domain.menu.dto.MenuDto;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateStoreDto {

    private String name;
    private String call;
    private String address;
    private String hours;

    List<HashtagDto> hashtagList;
    List<MenuDto> menuList;
}
