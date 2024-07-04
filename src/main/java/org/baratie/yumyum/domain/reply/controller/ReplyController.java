package org.baratie.yumyum.domain.reply.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.reply.dto.CreateReplyDto;
import org.baratie.yumyum.domain.reply.dto.ReplyResponseDto;
import org.baratie.yumyum.domain.reply.dto.UpdateRelyDto;
import org.baratie.yumyum.domain.reply.service.ReplyService;
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

    @GetMapping("/{reviewId}")
    public ResponseEntity<Slice<ReplyResponseDto>> getReplyOnReview(@PathVariable Long reviewId, @RequestParam int pageNumber) {
        reviewService.validationReviewId(reviewId);

        Pageable pageable = PageRequest.of(pageNumber, 5);
        Slice<ReplyResponseDto> replyOnReview = replyService.getReplyOnReview(reviewId, pageable);

        return new ResponseEntity<>(replyOnReview, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createReply(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CreateReplyDto request){
        replyService.createReply(customUserDetails, request);
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
