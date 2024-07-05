package org.baratie.yumyum.domain.review.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreReviewDto {

    private String profileImage;
    private String nickname;
    private double grade;
    private Long totalReviewCount;
    private double avgGrade;
    private String content;
}
