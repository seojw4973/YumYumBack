package org.baratie.yumyum.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.review.dto.ReviewAllDto;
import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;
import org.baratie.yumyum.domain.review.dto.StoreReviewDto;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.global.utils.file.repository.ImageRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewReadService {

    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;

    /**
     * 리뷰 전체 조회 (이미지 제외)
     * @param pageable
     * @return
     */
    public Slice<ReviewAllDto> getAllReview(Pageable pageable){
        return reviewRepository.findAllReviews(pageable);
    }

    /**
     * 가게 상세페이지 리뷰 조회
     * @param storeId
     * @param pageable
     * @return 가게에 달린 리뷰
     */
    public Slice<StoreReviewDto> getStoreReviewList(Long storeId, Pageable pageable) {
        return reviewRepository.findReviewByStoreId(storeId, pageable);
    }

    /**
     * 리뷰 상세조회
     * @param reviewId
     * @return 리뷰 상세정보 리턴
     */
    public ReviewDetailDto getReviewDetail(Long reviewId) {
        Long memberId = reviewRepository.findMemberIdByReviewId(reviewId);
        ReviewDetailDto reviewDetail = reviewRepository.findReviewDetail(memberId, reviewId);
        List<String> images = imageRepository.findByReviewId(reviewId);

        return reviewDetail.tranceDto(reviewDetail, images);
    }

}