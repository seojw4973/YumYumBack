package org.baratie.yumyum.domain.review.dto;

import lombok.*;
import org.baratie.yumyum.domain.member.dto.MemberBasicDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewDetailDto {

    private Long memberId;
    private String imageUrl;
    private String nickname;
    private Long totalReviewCount;
    private double avgGrade;

    private Long reviewId;
    private double grade;
    private String content;
    private LocalDateTime createdAt;
    private List<String> imageList;

    private String storeName;
    private String address;

    private Boolean likeStatus;

    public ReviewDetailDto tranceDto(MemberBasicDto memberBasicDto, ReviewDetailDto reviewDetail, List<String> imageList) {

        ReviewDetailDto result = ReviewDetailDto.builder()
                .memberId(memberBasicDto.getMemberId())
                .imageUrl(memberBasicDto.getProfileImage())
                .nickname(memberBasicDto.getNickname())
                .totalReviewCount(memberBasicDto.getTotalReviewCount())
                .avgGrade(memberBasicDto.getAvgGrade())
                .reviewId(reviewDetail.getReviewId())
                .imageList(imageList)
                .grade(reviewDetail.getGrade())
                .content(reviewDetail.getContent())
                .createdAt(reviewDetail.getCreatedAt())
                .storeName(reviewDetail.getStoreName())
                .address(reviewDetail.getAddress())
                .likeStatus(reviewDetail.getLikeStatus())
                .build();

        return result;
    }

    @Builder
    public ReviewDetailDto(Long reviewId, double grade, String content, LocalDateTime createdAt, String storeName, String address, Boolean likeStatus) {
        this.reviewId = reviewId;
        this.grade = grade;
        this.content = content;
        this.createdAt = createdAt;
        this.storeName = storeName;
        this.address = address;
        this.likeStatus = likeStatus;
    }
}

