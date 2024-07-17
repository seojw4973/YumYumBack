package org.baratie.yumyum.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Role;
import org.baratie.yumyum.domain.member.dto.SimpleMemberDto;
import org.baratie.yumyum.domain.member.service.AdminService;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.store.dto.AdminStoreDto;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 관리자 페이지 회원 전체 조회
     */
    @GetMapping("/member")
    public ResponseEntity<Page<SimpleMemberDto>> getAllMember(@PageableDefault(size = 5) Pageable pageable) {
        Page<SimpleMemberDto> simpleMemberDto = adminService.getSimpleMemberInfo(pageable);

        return new ResponseEntity<>(simpleMemberDto, HttpStatus.OK);
    }

    /**
     * 관리자 페이지 맛집 전체 조회
     */
    @GetMapping("/store")
    public ResponseEntity<Page<AdminStoreDto>> findAllAdmin(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AdminStoreDto> adminStoreDto = adminService.getAdminStores(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(adminStoreDto);
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
        adminService.deleteMember(memberId);

        return ResponseEntity.noContent().build();
    }

}
