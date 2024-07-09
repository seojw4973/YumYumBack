package org.baratie.yumyum.domain.store.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.hashtag.dto.HashtagDto;
import org.baratie.yumyum.domain.image.domain.Image;
import org.baratie.yumyum.domain.image.dto.ImageDto;
import org.baratie.yumyum.domain.menu.domain.Menu;
import org.baratie.yumyum.domain.menu.dto.MenuDto;
import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;
import org.baratie.yumyum.domain.store.domain.Store;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@ToString
public class StoreDetailDto {

    private Long storeId;
    private String name;
    private String address;
    private String hours;
    private String calls;
    private int views;
    private Double avgGrade;
    private BigDecimal latitude;
    private BigDecimal longitude;

    private List<Hashtag> hashtagList;
    private List<Menu> menuList;
    private List<String> imageList;

    private Long reviewCount;
    private Long favoriteCount;
    private Boolean favoriteStatus;

    public StoreDetailDto tranceDto(StoreDetailDto storeDetail, List<Hashtag> hashtagList, List<Menu> menuList, List<String> imageList) {
        StoreDetailDto result = StoreDetailDto.builder()
                .storeId(storeDetail.getStoreId())
                .name(storeDetail.getName())
                .address(storeDetail.getAddress())
                .hours(storeDetail.getHours())
                .calls(storeDetail.getCalls())
                .views(storeDetail.getViews())
                .avgGrade(Math.round(storeDetail.getAvgGrade()*10.0)/10.0)
                .latitude(storeDetail.getLatitude())
                .longitude(storeDetail.getLongitude())
                .reviewCount(storeDetail.getReviewCount())
                .favoriteCount(storeDetail.getFavoriteCount())
                .favoriteStatus(storeDetail.getFavoriteStatus())
                .build();

        result.imageList = imageList;
        result.hashtagList = hashtagList;
        result.menuList = menuList;

        return result;
    }

    @Builder
    public StoreDetailDto(Long storeId, String name, String address, String hours, String calls, int views, double avgGrade, BigDecimal latitude, BigDecimal longitude, Long reviewCount, Long favoriteCount, Boolean favoriteStatus) {
        this.storeId = storeId;
        this.name = name;
        this.address = address;
        this.hours = hours;
        this.calls = calls;
        this.views = views;
        this.avgGrade = avgGrade;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reviewCount = reviewCount;
        this.favoriteCount = favoriteCount;
        this.favoriteStatus = favoriteStatus;

    }
}
