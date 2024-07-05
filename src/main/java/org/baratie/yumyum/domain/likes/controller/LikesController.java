package org.baratie.yumyum.domain.likes.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.likes.dto.LikesDto;
import org.baratie.yumyum.domain.likes.service.LikesService;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.domain.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;
    private final MemberService memberService;
    private final ReviewService reviewService;

    @PostMapping("/like")
    public ResponseEntity<Void> checkLikes(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody LikesDto likesDto){
        Member member = memberService.getMember(customUserDetails.getId());
        System.out.println("member.getNickname() = " + member.getNickname());
        Review review = reviewService.getReview(likesDto.getReviewId());

        likesService.checkLikes(member, review, likesDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
