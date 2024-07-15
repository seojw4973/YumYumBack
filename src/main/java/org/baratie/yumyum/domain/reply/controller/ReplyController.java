package org.baratie.yumyum.domain.reply.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.dto.MyReplyDto;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.reply.dto.CreateReplyDto;
import org.baratie.yumyum.domain.reply.dto.ReplyResponseDto;
import org.baratie.yumyum.domain.reply.dto.UpdateRelyDto;
import org.baratie.yumyum.domain.reply.service.ReplyService;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.domain.review.service.ReviewService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    @GetMapping("/{reviewId}")
    public ResponseEntity<Slice<ReplyResponseDto>> getReplyOnReview(@PathVariable Long reviewId, @RequestParam int pageNumber) {
        reviewService.validationReviewId(reviewId);

        Pageable pageable = PageRequest.of(pageNumber, 5);
        Slice<ReplyResponseDto> replyOnReview = replyService.getReplyOnReview(reviewId, pageable);

        return new ResponseEntity<>(replyOnReview, HttpStatus.OK);
    }

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

    @PostMapping
    public ResponseEntity<Void> createReply(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CreateReplyDto request){
        Member member = memberService.getMember(customUserDetails.getId());
        Review review = reviewService.getReview(request.getReviewId());

        replyService.createReply(member, review, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{replyId}")
    public ResponseEntity<Void> updateReply(@PathVariable Long replyId, @RequestBody UpdateRelyDto request){
        replyService.updateReply(replyId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId){
        replyService.deleteReply(replyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
