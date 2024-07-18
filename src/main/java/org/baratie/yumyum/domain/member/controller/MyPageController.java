package org.baratie.yumyum.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.member.service.MyPageService;
import org.baratie.yumyum.global.utils.pageDto.CustomSliceDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MyPageController {
    private final MemberService memberService;
    private final MyPageService myPageService;

    /**
     * 내 댓글 보기
     */
    @GetMapping("/myReply")
    public ResponseEntity<CustomSliceDto> getMyReply(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable) {
        Long memberId = customUserDetails.getId();

        CustomSliceDto myReplyDto = myPageService.getMyReply(memberId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(myReplyDto);
    }

    /**
     * 좋아요한 리뷰 보기
     */
    @GetMapping("/likeReview")
    public ResponseEntity<CustomSliceDto> getLikeReview(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable){
        Long memberId = customUserDetails.getId();

        CustomSliceDto likeReviewDto = myPageService.getMyLikeReview(memberId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(likeReviewDto);
    }

    /**
     * 내가 작성한 리뷰
     */
    @GetMapping("/myReview")
    public ResponseEntity<CustomSliceDto> getMyReviewList(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable) {
        memberService.validationMemberId(customUserDetails.getId());

        CustomSliceDto myReview = myPageService.getMyReview(customUserDetails.getId(), pageable);
        return new ResponseEntity<>(myReview, HttpStatus.OK);
    }

    /**
     * 내가 즐겨찾기한 가게
     */
    @GetMapping("/favorite")
    ResponseEntity<CustomSliceDto> myFavoriteStore(@AuthenticationPrincipal CustomUserDetails customUserDetails,  Pageable pageable) {
        CustomSliceDto myFavoriteStore = myPageService.getMyFavoriteStore(customUserDetails.getId(), pageable);
        return new ResponseEntity<>(myFavoriteStore, HttpStatus.ACCEPTED);
    }
}
