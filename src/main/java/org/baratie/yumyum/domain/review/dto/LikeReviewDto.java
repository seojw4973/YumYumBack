package org.baratie.yumyum.domain.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
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
}
