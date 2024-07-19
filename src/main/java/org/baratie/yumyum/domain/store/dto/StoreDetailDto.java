package org.baratie.yumyum.domain.store.dto;

import lombok.*;
import org.baratie.yumyum.domain.category.dto.CategoryDto;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.menu.domain.Menu;

import java.math.BigDecimal;
import java.util.List;

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

    private List<String> hashtagList;
    private List<Menu> menuList;
    private List<String> imageList;
    private List<String> categoryList;

    private Long totalReviewCount;
    private Long totalFavoriteCount;
    private Boolean favoriteStatus;

    public StoreDetailDto tranceDto(StoreDetailDto storeDetail, List<String> hashtagList, List<Menu> menuList, List<String> imageList, List<String> categoryList) {
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
                .totalReviewCount(storeDetail.getTotalReviewCount())
                .totalFavoriteCount(storeDetail.getTotalFavoriteCount())
                .favoriteStatus(storeDetail.getFavoriteStatus())
                .build();

        result.imageList = imageList;
        result.hashtagList = hashtagList;
        result.menuList = menuList;
        result.categoryList = categoryList;

        return result;
    }

    @Builder
    public StoreDetailDto(Long storeId, String name, String address, String hours, String calls, int views, double avgGrade, BigDecimal latitude, BigDecimal longitude, Long totalReviewCount, Long totalFavoriteCount, Boolean favoriteStatus) {
        this.storeId = storeId;
        this.name = name;
        this.address = address;
        this.hours = hours;
        this.calls = calls;
        this.views = views;
        this.avgGrade = avgGrade;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalReviewCount = totalReviewCount;
        this.totalFavoriteCount = totalFavoriteCount;
        this.favoriteStatus = favoriteStatus;

    }
}
