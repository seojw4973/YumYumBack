package org.baratie.yumyum.domain.reply.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.reply.dto.CreateReplyDto;
import org.baratie.yumyum.domain.reply.dto.UpdateRelyDto;
import org.baratie.yumyum.domain.reply.service.ReplyService;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.domain.review.service.ReviewService;
import org.baratie.yumyum.global.utils.pageDto.CustomSliceDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;
    private final ReviewService reviewService;
    private final MemberService memberService;

    /**
     * 리뷰에 달린 댓글 조회
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<CustomSliceDto> getReplyOnReview(@PathVariable Long reviewId, Pageable pageable) {
        reviewService.validationReviewId(reviewId);

        CustomSliceDto replyOnReview = replyService.getReplyOnReview(reviewId, pageable);

        return new ResponseEntity<>(replyOnReview, HttpStatus.OK);
    }

    /**
     * 댓글 달기
     */
    @PostMapping
    public ResponseEntity<Void> createReply(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody CreateReplyDto request){
        Member member = memberService.getMember(customUserDetails.getId());
        Review review = reviewService.getReview(request.getReviewId());

        replyService.createReply(member, review, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 댓글 수정
     */
    @PatchMapping("/{replyId}")
    public ResponseEntity<Void> updateReply(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long replyId, @Valid @RequestBody UpdateRelyDto request) {
        memberService.validationMemberId(customUserDetails.getId());

        replyService.updateReply(customUserDetails.getId(), replyId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long replyId){
        memberService.validationMemberId(customUserDetails.getId());

        replyService.deleteReply(customUserDetails.getId(), replyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
