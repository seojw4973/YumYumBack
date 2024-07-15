package org.baratie.yumyum.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.baratie.yumyum.global.utils.file.dto.ImageDto;

import java.util.List;

@Getter
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
    private List<String> images;

    public void addImageList(List<String> images){
        this.images = images;
    }

    public ReviewAllDto(Long reviewId, String profileImage, String storeName, String address, String nickname, double grade, Long totalReviewCount, double avgGrade, String content) {
        this.reviewId = reviewId;
        this.profileImage = profileImage;
        this.storeName = storeName;
        this.address = address;
        this.nickname = nickname;
        this.grade = grade;
        this.totalReviewCount = totalReviewCount;
        this.avgGrade = Math.round(avgGrade*10.0)/10.0;
        this.content = content;
    }


}
