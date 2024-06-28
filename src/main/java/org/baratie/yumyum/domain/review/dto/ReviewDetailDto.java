package org.baratie.yumyum.domain.review.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class ReviewDetailDto {
    private String imageUrl;
    private String nickname;
    private Long totalReviewCount;
    private double avgGrade;
    private String storeName;
    private String address;
    private double grade;
    private String content;
    private List<String> images;

    public ReviewDetailDto tranceDto(ReviewDetailDto reviewDetail, List<String> images) {

        ReviewDetailDto result = ReviewDetailDto.builder()
                .imageUrl(reviewDetail.getImageUrl())
                .nickname(reviewDetail.getNickname())
                .totalReviewCount(reviewDetail.getTotalReviewCount())
                .avgGrade(Math.round(reviewDetail.getAvgGrade() * 10.0) / 10.0)
                .storeName(reviewDetail.getStoreName())
                .address(reviewDetail.getAddress())
                .grade(reviewDetail.getGrade())
                .content(reviewDetail.getContent())
                .build();

        result.images = images;

        return result;
    }

    @Builder
    public ReviewDetailDto(String imageUrl, String nickname, Long totalReviewCount, double avgGrade, String storeName, String address, double grade, String content) {
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.totalReviewCount = totalReviewCount;
        this.avgGrade = avgGrade;
        this.storeName = storeName;
        this.address = address;
        this.grade = grade;
        this.content = content;
    }
}

