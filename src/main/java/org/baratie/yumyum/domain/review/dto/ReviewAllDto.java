package org.baratie.yumyum.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baratie.yumyum.domain.image.dto.ImageDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAllDto {

    private String name;
    private String address;

    private String nickname;
    private double grade;

    private Long reviewCount;
    private double avgGrade;

    private String content;

}
