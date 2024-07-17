package org.baratie.yumyum.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;
import org.baratie.yumyum.domain.review.service.ReviewReadService;
import org.baratie.yumyum.domain.review.service.ReviewService;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.baratie.yumyum.global.utils.pageDto.CustomSliceDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public CustomSliceDto getAllReview(@RequestParam("pageNumber") int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber, 5);

        return reviewReadService.getAllReview(pageable);
    }

    /**
     * 리뷰 상세 조회
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDetailDto> getReviewDetail(@PathVariable Long reviewId) {
        reviewService.validationReviewId(reviewId);

        ReviewDetailDto reviewDetail = reviewReadService.getReviewDetail(reviewId);

        return new ResponseEntity<>(reviewDetail, HttpStatus.OK);
    }

    /**
     * 가게에 작성된 리뷰 리스트
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<CustomSliceDto> getStoreReviewList(@PathVariable Long storeId, @RequestParam int pageNumber) {
        storeService.existStoreId(storeId);
        Pageable pageable = PageRequest.of(pageNumber, 5);

        CustomSliceDto storeReviewList = reviewReadService.getStoreReviewList(storeId, pageable);

        return new ResponseEntity<>(storeReviewList, HttpStatus.ACCEPTED);
    }
}
