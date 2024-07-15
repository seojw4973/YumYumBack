package org.baratie.yumyum.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.dto.MyReplyDto;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.reply.service.ReplyService;
import org.baratie.yumyum.domain.review.dto.LikeReviewDto;
import org.baratie.yumyum.domain.review.dto.MyReviewDto;
import org.baratie.yumyum.domain.review.service.ReviewService;
import org.baratie.yumyum.domain.store.dto.MyFavoriteStoreDto;
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
@RequestMapping("/member")
public class MyPageController {
    private final MemberService memberService;
    private final ReplyService replyService;
    private final ReviewService reviewService;
    private final StoreService storeService;

    /**
     * 내 댓글 보기
     * @param customUserDetails 로그인한 유저
     * @param pageNumber 페이지 번호
     * @return 로그인한 유저 id의 댓글 리턴
     */
    @GetMapping("/myReply")
    public ResponseEntity<Slice<MyReplyDto>> getMyReply(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam int pageNumber) {
        Long memberId = customUserDetails.getId();
        Pageable pageable = PageRequest.of(pageNumber, 5);
        Slice<MyReplyDto> myReplyDto = replyService.getMyReply(memberId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(myReplyDto);
    }

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
     * 내가 즐겨찾기한 가게
     */
    @GetMapping("/favorite")
    ResponseEntity<Slice<MyFavoriteStoreDto>> myFavoriteStore(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 5);

        Slice<MyFavoriteStoreDto> myFavoriteStore = storeService.getMyFavoriteStore(customUserDetails.getId(), pageable);

        return new ResponseEntity<>(myFavoriteStore, HttpStatus.ACCEPTED);
    }

}
