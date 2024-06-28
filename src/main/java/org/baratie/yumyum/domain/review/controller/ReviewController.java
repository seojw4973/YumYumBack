package org.baratie.yumyum.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;
import org.baratie.yumyum.domain.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{storeId}/review/{reviewId}")
    public ResponseEntity<ReviewDetailDto> getReviewDetail(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long storeId, @PathVariable Long reviewId) {
        ReviewDetailDto reviewDetail = reviewService.getReviewDetail(customUserDetails, reviewId);

        return new ResponseEntity<>(reviewDetail, HttpStatus.ACCEPTED);
    }
}
