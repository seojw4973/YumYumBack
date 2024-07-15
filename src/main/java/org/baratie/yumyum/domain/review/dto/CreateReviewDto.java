package org.baratie.yumyum.domain.review.dto;

import lombok.*;
import org.baratie.yumyum.global.utils.file.domain.Image;
import org.baratie.yumyum.global.utils.file.dto.ImageDto;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.domain.store.domain.Store;
import org.springframework.web.multipart.MultipartFile;

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

    public Review toEntity(Store store, Member member) {

        return Review.builder()
                .content(content)
                .grade(grade)
                .store(store)
                .member(member)
                .build();
    }

}
