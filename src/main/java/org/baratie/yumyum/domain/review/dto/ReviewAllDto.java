package org.baratie.yumyum.domain.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewAllDto {
    private Long reviewId;
    private String imageUrl;
    private String storeName;
    private String address;

    private String nickname;
    private double grade;

    private Long totalReviewCount;
    private double avgGrade;

    private String content;
    private Boolean likeStatus;
    private List<String> imageList;

    public void addImageList(List<String> imageList){
        this.imageList = imageList;
    }

    @Builder
    public ReviewAllDto(Long reviewId, String imageUrl, String storeName, String address, String nickname, double grade, Long totalReviewCount, double avgGrade, String content, Boolean likeStatus) {
        this.reviewId = reviewId;
        this.imageUrl = imageUrl;
        this.storeName = storeName;
        this.address = address;
        this.nickname = nickname;
        this.grade = grade;
        this.totalReviewCount = totalReviewCount;
        this.avgGrade = Math.round(avgGrade*10.0)/10.0;
        this.content = content;
        this.likeStatus = likeStatus;
    }


}
