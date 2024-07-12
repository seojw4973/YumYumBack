package org.baratie.yumyum.domain.review.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class ReviewDetailDto {

    private Long reviewId;
    private Long memberId;
    private String imageUrl;
    private String nickname;
    private Long totalReviewCount;
    private double avgGrade;
    private String storeName;
    private String address;
    private double grade;
    private String content;
    private Boolean likeStatus;
    private LocalDateTime createdAt;
    private List<String> images;

    public ReviewDetailDto tranceDto(ReviewDetailDto reviewDetail, List<String> images) {

        ReviewDetailDto result = ReviewDetailDto.builder()
                .reviewId(reviewDetail.getReviewId())
                .memberId(reviewDetail.getMemberId())
                .imageUrl(reviewDetail.getImageUrl())
                .nickname(reviewDetail.getNickname())
                .totalReviewCount(reviewDetail.getTotalReviewCount())
                .avgGrade(Math.round(reviewDetail.getAvgGrade() * 10.0) / 10.0)
                .storeName(reviewDetail.getStoreName())
                .address(reviewDetail.getAddress())
                .grade(reviewDetail.getGrade())
                .content(reviewDetail.getContent())
                .likeStatus(reviewDetail.getLikeStatus())
                .createdAt(reviewDetail.getCreatedAt())
                .build();

        result.images = images;

        return result;
    }

    @Builder
    public ReviewDetailDto(Long reviewId, Long memberId, String imageUrl, String nickname, Long totalReviewCount, double avgGrade, String storeName, String address, double grade, String content, Boolean likeStatus, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.memberId = memberId;
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.totalReviewCount = totalReviewCount;
        this.avgGrade = avgGrade;
        this.storeName = storeName;
        this.address = address;
        this.grade = grade;
        this.content = content;
        this.likeStatus = likeStatus;
        this.createdAt = createdAt;
    }
}

