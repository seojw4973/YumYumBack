package org.baratie.yumyum.domain.review.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreReviewDto {

    private String profileImage;
    private String nickname;
    private double grade;
    private Long totalReviewCount;
    private double avgGrade;
    private String content;

    @Builder
    public StoreReviewDto(String profileImage, String nickname, double grade, Long totalReviewCount, double avgGrade, String content) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.grade = grade;
        this.totalReviewCount = totalReviewCount;
        this.avgGrade =  Math.round(avgGrade * 10.0) / 10.0;
        this.content = content;
    }
}
