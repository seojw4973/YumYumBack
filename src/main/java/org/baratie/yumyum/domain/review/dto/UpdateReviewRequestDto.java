package org.baratie.yumyum.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateReviewRequestDto {

    @NotBlank
    private String content;

    @NotBlank
    private double grade;

}
