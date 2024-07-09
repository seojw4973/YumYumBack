package org.baratie.yumyum.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baratie.yumyum.global.utils.file.dto.ImageDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAllDto {
    private Long reviewId;
    private String profileImage;
    private String storeName;
    private String address;

    private String nickname;
    private double grade;

    private Long totalReviewCount;
    private double avgGrade;

    private String content;
    private List<ImageDto> images;

}
