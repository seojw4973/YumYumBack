package org.baratie.yumyum.domain.store.dto;

import lombok.*;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.image.domain.Image;
import org.baratie.yumyum.domain.menu.domain.Menu;
import org.baratie.yumyum.domain.store.domain.Store;

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

    List<Image> imageList;
    List<Hashtag> hashtagList;
    List<Menu> menuList;
}
