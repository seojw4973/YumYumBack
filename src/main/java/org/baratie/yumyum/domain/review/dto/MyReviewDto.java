package org.baratie.yumyum.domain.review.dto;

import lombok.*;

import java.io.PrintWriter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MyReviewDto {

    private String storeName;
    private String address;
    private String nickname;
    private double grade;
    private Long reviewTotalCount;
    private double avgGrade;
    private String content;
    private Boolean likeStatus;
}
