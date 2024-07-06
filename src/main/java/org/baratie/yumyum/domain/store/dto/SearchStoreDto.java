package org.baratie.yumyum.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private List<Hashtag> hashtags;
    private String categoryName;

//    public SearchStoreDto(Long storeId, String name, String imageUrl, String address, int views, Double avgGrade, Long totalReviewCount, Long totalFavoriteCount, Boolean favoriteStatus, String categoryName){
//        this.storeId = storeId;
//        this.name = name;
//        this.imageUrl = imageUrl;
//        this.address = address;
//        this.views = views;
//        this.avgGrade = avgGrade;
//        this.totalReviewCount = totalReviewCount;
//        this.totalFavoriteCount = totalFavoriteCount;
//        this.favoriteStatus = favoriteStatus;
//        this.categoryName = categoryName;
//
//    }
}
