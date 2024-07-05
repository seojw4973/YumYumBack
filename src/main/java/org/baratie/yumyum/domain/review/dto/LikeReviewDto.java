package org.baratie.yumyum.domain.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LikeReviewDto {
    private Long id;
    private String imageUrl;
    private String nickName;
    private double grade;
    private Boolean isLikes;
    private String content;
    private LocalDateTime createdAt;
}
