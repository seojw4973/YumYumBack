package org.baratie.yumyum.domain.store.dto;

import com.google.maps.errors.ApiException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    private String name;
    private String address;
    private String calls;
    private String hours;
    private boolean isClosed;
    private BigDecimal latitude;
    private BigDecimal longitude;

    List<HashtagDto> hashtagList;
    List<MenuDto> menuList;

    public Store toEntity() throws IOException, InterruptedException, ApiException {
        List<Hashtag> hashtagList = this.hashtagList.stream().map(HashtagDto::toEntity).collect(Collectors.toList());
        List<Menu> menuList = this.menuList.stream().map(MenuDto::toEntity).collect(Collectors.toList());

        BigDecimal[] bigDecimal = GeoUtils.findGeoPoint(this.address);
        BigDecimal lat = bigDecimal[0];
        BigDecimal lng = bigDecimal[1];

        return Store.builder()
                .name(this.name)
                .address(this.address)
                .call(this.calls)
                .hours(this.hours)
                .isClosed(this.isClosed)
                .latitude(lat)
                .longitude(lng)
                .hashtagList(hashtagList)
                .menuList(menuList)
                .build();
    }
}