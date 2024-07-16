package org.baratie.yumyum.domain.store.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyFavoriteStoreDto {

    private Long storeId;
    private String name;
    private String imageUrl;
    private String address;
    private int views;
    private Double avgGrade;
    private Long totalReviewCount;
    private Long totalFavoriteCount;
    private Boolean favoriteStatus;
    private List<String> hashtags;
    private List<String> images;
    private String categoryName;

    public void addHashtagList(List<String> hashtags) {
        this.hashtags = hashtags;
    }
    public void addImageList(List<String> images) {this.images = images;}

    @Builder
    public MyFavoriteStoreDto(Long storeId, String name, String imageUrl, String address, int views, double avgGrade, Long totalReviewCount, Long totalFavoriteCount, Boolean favoriteStatus, String categoryName) {
        this.storeId = storeId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.address = address;
        this.views = views;
        this.avgGrade = Math.round(avgGrade * 10.0) / 10.0;;
        this.totalReviewCount = totalReviewCount;
        this.totalFavoriteCount = totalFavoriteCount;
        this.favoriteStatus = favoriteStatus;
        this.categoryName = categoryName;
    }
}
