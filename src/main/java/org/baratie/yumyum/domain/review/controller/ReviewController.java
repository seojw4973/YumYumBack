package org.baratie.yumyum.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.review.dto.*;
import org.baratie.yumyum.domain.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;
    /**
     * 리뷰 작성
     */
    @PostMapping
    public ResponseEntity<Void> writeReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @Valid @RequestPart("createReviewDto") CreateReviewDto createReviewDto,
                                            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        Member member = memberService.getMember(customUserDetails.getId());

        reviewService.createReview(member, createReviewDto, files);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 리뷰 수정
     */
    @PatchMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @PathVariable Long reviewId,
                                             @Valid @RequestPart UpdateReviewRequestDto updateReviewRequestDto,
                                             @RequestPart(required = false) List<MultipartFile> files) {
        reviewService.updateReview(customUserDetails.getId(), reviewId, updateReviewRequestDto, files);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long reviewId) {
        reviewService.deleteReview(customUserDetails.getId(), reviewId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
