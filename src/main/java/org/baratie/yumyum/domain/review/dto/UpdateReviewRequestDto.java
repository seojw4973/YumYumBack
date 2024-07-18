package org.baratie.yumyum.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateReviewRequestDto {

    @NotBlank
    private String content;

    @NotNull
    private double grade;

}
