package org.baratie.yumyum.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.service.AdminService;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.global.utils.pageDto.CustomPageDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final MemberService memberService;

    /**
     * 관리자 페이지 회원 전체 조회
     */
    @GetMapping("/member")
    public ResponseEntity<CustomPageDto> getAllMember(Pageable pageable) {
        CustomPageDto simpleMemberPage = adminService.getSimpleMemberInfo(pageable);

        return new ResponseEntity<>(simpleMemberPage, HttpStatus.OK);
    }

    /**
     * 관리자 페이지 맛집 전체 조회
     */
    @GetMapping("/store")
    public ResponseEntity<CustomPageDto> findAllAdmin(Pageable pageable) {
        CustomPageDto adminStorePage = adminService.getAdminStores(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(adminStorePage);
    }

    /**
     * 가게 삭제
     */
    @DeleteMapping("/store/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId) {
        adminService.deleteStore(storeId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        adminService.deleteReview(reviewId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId) {
        adminService.deleteReply(replyId);

        return ResponseEntity.noContent().build();
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/member/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        Member member = memberService.getMember(memberId);
        adminService.deleteMember(member);

        return ResponseEntity.noContent().build();
    }
}
