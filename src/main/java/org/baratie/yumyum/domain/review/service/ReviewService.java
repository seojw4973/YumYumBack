package org.baratie.yumyum.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.image.repository.ImageRepository;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;
import org.baratie.yumyum.domain.review.dto.UpdateReviewRequestDto;
import org.baratie.yumyum.domain.review.exception.ReviewNotFoundException;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;

    public ReviewDetailDto getReviewDetail(CustomUserDetails customUserDetails, Long reviewId) {
        validationReviewId(reviewId);

        ReviewDetailDto reviewDetail = reviewRepository.findReviewDetail(customUserDetails.getId(), reviewId);
        List<String> images = imageRepository.findByReviewId(reviewId);
        
        ReviewDetailDto reviewDetailDto = reviewDetail.tranceDto(reviewDetail, images);

        return reviewDetailDto;
    }

    /**
     * 리뷰 수정
     * @param reviewId 수정할 리뷰
     * @param request 수정 내용
     */
    public void updateReview(Long reviewId, UpdateReviewRequestDto request) {
        validationReviewId(reviewId);

        Review findReview = getReview(reviewId);
        Review updateReview = findReview.updateReview(request);

        reviewRepository.save(updateReview);
    }

    /**
     * 리뷰 삭제
     * @param reviewId 삭제할 리뷰
     */
    public void deleteReview(Long reviewId) {
        validationReviewId(reviewId);

        reviewRepository.deleteById(reviewId);
    }

    /**
     * 리뷰가 존재하는지 확인
     * @param reviewId 검증할 reviewId
     * @return Review
     */
    public boolean validationReviewId(Long reviewId) {

        boolean exists = reviewRepository.existsById(reviewId);
        if (!exists) {
            throw new ReviewNotFoundException(ErrorCode.REVIEW_NOT_FOUND);
        }

        return true;
    }

    /**
     * @param reviewId 리뷰 객체를 받아오기 위한 id
     * @return Review
     */
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).get();
    }

}
