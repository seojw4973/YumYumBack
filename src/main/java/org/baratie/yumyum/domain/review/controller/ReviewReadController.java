package org.baratie.yumyum.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;
import org.baratie.yumyum.domain.review.service.ReviewReadService;
import org.baratie.yumyum.domain.review.service.ReviewService;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.baratie.yumyum.global.utils.pageDto.CustomSliceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewReadController {

    private final StoreService storeService;
    private final ReviewService reviewService;
    private final ReviewReadService reviewReadService;

    /**
     * 리뷰 전체 조회
     */
    @GetMapping
    public CustomSliceDto getAllReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable){
        Long memberId = customUserDetails.getId();
        return reviewReadService.getAllReview(memberId, pageable);
    }

    /**
     * 리뷰 상세 조회
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDetailDto> getReviewDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long reviewId) {
        reviewService.validationReviewId(reviewId);
        ReviewDetailDto reviewDetail = reviewReadService.getReviewDetail(customUserDetails.getId(), reviewId);

        return new ResponseEntity<>(reviewDetail, HttpStatus.OK);
    }

    /**
     * 가게에 작성된 리뷰 리스트
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<CustomSliceDto> getStoreReviewList(@PathVariable Long storeId, Pageable pageable) {
        storeService.existStoreId(storeId);

        CustomSliceDto storeReviewList = reviewReadService.getStoreReviewList(storeId, pageable);

        return new ResponseEntity<>(storeReviewList, HttpStatus.ACCEPTED);
    }
}
