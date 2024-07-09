package org.baratie.yumyum.domain.review.dto;

import lombok.*;
import org.baratie.yumyum.global.utils.file.domain.Image;
import org.baratie.yumyum.global.utils.file.dto.ImageDto;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.domain.store.domain.Store;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateReviewDto {
    private Long storeId;
    private String content;
    private double grade;
    List<ImageDto> imageList;

    public Review toEntity(Store store, Member member) {
        List<Image> images = this.imageList.stream().map(ImageDto::toEntity).collect(Collectors.toList());

        return Review.builder()
                .content(content)
                .grade(grade)
                .imageList(images)
                .store(store)
                .member(member)
                .build();


    }

}
