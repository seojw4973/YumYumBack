package org.baratie.yumyum.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;
import org.baratie.yumyum.domain.review.dto.UpdateReviewRequestDto;
import org.baratie.yumyum.domain.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDetailDto> getReviewDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long reviewId) {
        ReviewDetailDto reviewDetail = reviewService.getReviewDetail(customUserDetails, reviewId);

        return new ResponseEntity<>(reviewDetail, HttpStatus.OK);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable Long reviewId, @RequestBody UpdateReviewRequestDto request) {
        reviewService.updateReview(reviewId, request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
