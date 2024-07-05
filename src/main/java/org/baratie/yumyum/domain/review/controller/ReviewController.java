package org.baratie.yumyum.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.review.dto.*;
import org.baratie.yumyum.domain.review.service.ReviewService;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final StoreService storeService;

    /**
     * 리뷰 전체 조회
     */
    @GetMapping("/review")
    public Slice<ReviewAllDto> getAllReview(@RequestParam("pageNumber") int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber, 5);

        return reviewService.getAllReview(pageable);
    }

    /**
     * 리뷰 상세 조회
     */
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<ReviewDetailDto> getReviewDetail(@PathVariable Long reviewId) {
        ReviewDetailDto reviewDetail = reviewService.getReviewDetail(reviewId);

        return new ResponseEntity<>(reviewDetail, HttpStatus.OK);
    }

    /**
     * 가게에 작성된 리뷰 리스트
     */
    @GetMapping("/store/{storeId}/review")
    public ResponseEntity<Slice<StoreReviewDto>> getStoreReviewList(@PathVariable Long storeId, @RequestParam int pageNumber) {
        Store store = storeService.validationStoreId(storeId);

        Pageable pageable = PageRequest.of(pageNumber, 5);

        Slice<StoreReviewDto> storeReviewList = reviewService.getStoreReviewList(store.getId(), pageable);

        return new ResponseEntity<>(storeReviewList, HttpStatus.ACCEPTED);
    }

    /**
     * 리뷰 작성
     */
    @PostMapping("/review")
    public ResponseEntity<Void> writeReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CreateReviewDto createReviewDto){
        reviewService.createReview(customUserDetails, createReviewDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 리뷰 수정
     */
    @PatchMapping("/review/{reviewId}")
    public ResponseEntity<Void> updateReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long reviewId, @RequestBody UpdateReviewRequestDto request) {
        reviewService.updateReview(customUserDetails.getId(), reviewId, request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long reviewId) {
        reviewService.deleteReview(customUserDetails.getId(), reviewId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
