package org.baratie.yumyum.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.review.dto.LikeReviewDto;
import org.baratie.yumyum.domain.review.dto.*;
import org.baratie.yumyum.domain.review.service.ReviewService;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.baratie.yumyum.global.utils.file.service.ImageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final StoreService storeService;
    private final MemberService memberService;



    /**
     * 좋아요한 리뷰 보기
     */
    @GetMapping("/likeReview")
    public ResponseEntity<Slice<LikeReviewDto>> getLikeReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam int pageNumber){
        Long memberId = customUserDetails.getId();
        Pageable pageable = PageRequest.of(pageNumber, 5);
        Slice<LikeReviewDto> likeReviewDto = reviewService.getMyLikeReview(memberId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(likeReviewDto);
    }

    /**
     * 내가 작성한 리뷰
     */
    @GetMapping("/myReview")
    public ResponseEntity<Slice<MyReviewDto>> getMyReviewList(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam int pageNumber) {
        memberService.validationMemberId(customUserDetails.getId());
        Pageable pageable = PageRequest.of(pageNumber, 5);

        Slice<MyReviewDto> myReview = reviewService.getMyReview(customUserDetails.getId(), pageable);

        return new ResponseEntity<>(myReview, HttpStatus.OK);
    }


    /**
     * 리뷰 작성
     */
    @PostMapping
    public ResponseEntity<Void> writeReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @RequestPart("createReviewDto") CreateReviewDto createReviewDto,
                                            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        reviewService.createReview(customUserDetails, createReviewDto, files);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 리뷰 수정
     */
    @PatchMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @PathVariable Long reviewId,
                                             @RequestPart UpdateReviewRequestDto request,
                                             @RequestPart List<MultipartFile> files) {
        reviewService.updateReview(customUserDetails.getId(), reviewId, request, files);

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
