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
    private String imageUrl;
    private String nickName;
    private double grade;
    private Boolean likeStatus;
    private String content;
    private LocalDateTime createdAt;
}
