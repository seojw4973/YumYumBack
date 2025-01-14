package org.baratie.yumyum.domain.store.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class SearchStoreDto {

    private Long storeId;
    private String name;
    private String address;
    private int views;
    private Double avgGrade;
    private Long totalReviewCount;
    private Long totalFavoriteCount;
    private Boolean favoriteStatus;
    private String categoryName;
    private BigDecimal longitude;
    private BigDecimal latitude;

    private List<String> hashtagList;
    private String imageUrl;


    public void addHashtagList(List<String> hashtagList) {
        this.hashtagList = hashtagList;
    }

    public void addImage(String image) {
        this.imageUrl = image;
    }

    public SearchStoreDto(Long storeId, String name, String address, int views, Double avgGrade, Long totalReviewCount, Long totalFavoriteCount, Boolean favoriteStatus, String categoryName, BigDecimal longitude, BigDecimal latitude){
        this.storeId = storeId;
        this.name = name;
        this.address = address;
        this.views = views;
        this.avgGrade = avgGrade;
        this.totalReviewCount = totalReviewCount;
        this.totalFavoriteCount = totalFavoriteCount;
        this.favoriteStatus = favoriteStatus;
        this.categoryName = categoryName;
        this.longitude = longitude;
        this.latitude = latitude;

    }
}
