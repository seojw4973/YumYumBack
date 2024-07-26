package org.baratie.yumyum.domain.review.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class LikeReviewDto {

    private Long reviewId;
    private String storeName;
    private String address;
    private String nickname;
    private String imageUrl;
    private double grade;
    private Long likeReviewCount;
    private double avgGrade;
    private Long replyCount;
    private String content;
    private Boolean likeStatus;
    private List<String> imageList;

    public void addImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    @Builder
    public LikeReviewDto(Long reviewId, String storeName, String address, String nickname, String imageUrl,double grade, Long likeReviewCount, double avgGrade, Long replyCount, String content, Boolean likeStatus) {
        this.reviewId = reviewId;
        this.storeName = storeName;
        this.address = address;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.grade = grade;
        this.likeReviewCount = likeReviewCount;
        this.avgGrade = Math.round(avgGrade*10.0)/10.0;
        this.replyCount = replyCount;
        this.content = content;
        this.likeStatus = likeStatus;
    }
}
