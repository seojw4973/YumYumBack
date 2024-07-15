package org.baratie.yumyum.domain.review.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class LikeReviewDto {

    private Long reviewId;
    private String storeName;
    private String address;
    private String nickname;
    private double grade;
    private Long reviewTotalCount;
    private double avgGrade;
    private String content;
    private Boolean likeStatus;
    private List<String> images;

    public void addImageList(List<String> images) {
        this.images = images;
    }

    @Builder
    public LikeReviewDto(Long reviewId, String storeName, String address, String nickname, double grade, Long reviewTotalCount, double avgGrade, String content, Boolean likeStatus) {
        this.reviewId = reviewId;
        this.storeName = storeName;
        this.address = address;
        this.nickname = nickname;
        this.grade = grade;
        this.reviewTotalCount = reviewTotalCount;
        this.avgGrade = Math.round(avgGrade*10.0)/10.0;
        this.content = content;
        this.likeStatus = likeStatus;
    }
}
