package org.baratie.yumyum.domain.store.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyFavoriteStoreDto {

    private Long storeId;
    private String name;
    private String address;
    private int views;
    private Double avgGrade;
    private Long totalReviewCount;
    private Long myFavoriteCount;
    private Long storeFavoriteCount;
    private Boolean favoriteStatus;
    private List<String> hashtagList;
    private String imageUrl;
    private String categoryName;

    public void addHashtagList(List<String> hashList) {
        this.hashtagList = hashList;
    }

    public void addImage(String image) {
        this.imageUrl = image;
    }

    @Builder
    public MyFavoriteStoreDto(Long storeId, String name, String address, int views, double avgGrade, Long totalReviewCount, Long myFavoriteCount, Long storeFavoriteCount, Boolean favoriteStatus, String categoryName) {
        this.storeId = storeId;
        this.name = name;
        this.address = address;
        this.views = views;
        this.avgGrade = Math.round(avgGrade * 10.0) / 10.0;;
        this.totalReviewCount = totalReviewCount;
        this.myFavoriteCount = myFavoriteCount;
        this.storeFavoriteCount = storeFavoriteCount;
        this.favoriteStatus = favoriteStatus;
        this.categoryName = categoryName;
    }
}
