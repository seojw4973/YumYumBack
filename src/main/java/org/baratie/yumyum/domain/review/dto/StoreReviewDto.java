package org.baratie.yumyum.domain.review.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreReviewDto {

    private Long memberId;
    private String imageUrl;
    private String nickname;
    private Long reviewId;
    private double grade;
    private String content;
    private Long totalReviewCount;
    private double avgGrade;
    private Boolean likeStatus;
    private List<String> imageList;

    public void addImage(List<String> imageList) {
        this.imageList = imageList;
    }

    @Builder
    public StoreReviewDto(Long memberId, String imageUrl, String nickname, Long reviewId, double grade, String content, Long totalReviewCount, double avgGrade, Boolean likeStatus) {
        this.memberId = memberId;
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.reviewId = reviewId;
        this.grade = grade;
        this.content = content;
        this.totalReviewCount = totalReviewCount;
        this.avgGrade =  Math.round(avgGrade * 10.0) / 10.0;
        this.likeStatus = likeStatus;
    }
}
