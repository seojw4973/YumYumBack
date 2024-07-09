package org.baratie.yumyum.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class SearchStoreDto {

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
    private String categoryName;
    private BigDecimal longitude;
    private BigDecimal latitude;


    public void addHashtagList(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public SearchStoreDto(Long storeId, String name, String imageUrl, String address, int views, Double avgGrade, Long totalReviewCount, Long totalFavoriteCount, Boolean favoriteStatus, String categoryName, BigDecimal longitude, BigDecimal latitude){
        this.storeId = storeId;
        this.name = name;
        this.imageUrl = imageUrl;
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
