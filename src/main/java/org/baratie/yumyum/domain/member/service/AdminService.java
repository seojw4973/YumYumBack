package org.baratie.yumyum.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.likes.repository.LikesRepository;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.dto.SimpleMemberDto;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.baratie.yumyum.domain.reply.repository.ReplyRepository;
import org.baratie.yumyum.domain.report.repository.ReportRepository;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.domain.store.dto.AdminStoreDto;
import org.baratie.yumyum.domain.store.repository.StoreRepository;
import org.baratie.yumyum.global.utils.pageDto.CustomPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final ReplyRepository replyRepository;
    private final LikesRepository likesRepository;
    private final ReportRepository reportRepository;

    /**
     * 관리자페이지 회원 조회
     */
    @Transactional(readOnly = true)
    public CustomPageDto getSimpleMemberInfo(Pageable pageable) {
        Page<SimpleMemberDto> simpleMemberInfo = memberRepository.getSimpleMemberInfo(pageable);

        return new CustomPageDto(simpleMemberInfo);
    }

    /**
     * 관리자 페이지 맛집 전체 조회
     */
    @Transactional(readOnly = true)
    public CustomPageDto getAdminStores(Pageable pageable) {
        Page<AdminStoreDto> simpleStore = storeRepository.getSimpleStore(pageable);

        return new CustomPageDto(simpleStore);
    }

    /**
     * 관리자 회원 탈퇴
     * @param member
     */
    public void deleteMember(Member member) {
        deleteReportId(member.getId());

        Member deletedMember = member.deleteMember(member.getId());
        memberRepository.save(deletedMember);
    }

    /**
     * 관리자 가게 삭제
     * @param storeId 삭제할 가게
     */
    public void deleteStore(Long storeId) {
        deleteReportId(storeId);

        storeRepository.deleteById(storeId);
    }

    /**
     * 관리자 리뷰 삭제
     * @param reviewId 삭제할 리뷰
     */
    public void deleteReview(Long reviewId) {
        deleteReportId(reviewId);

        likesRepository.deleteByReviewId(reviewId);
        reviewRepository.deleteById(reviewId);
    }

    /**
     * 관리자 댓글 삭제
     * @param replyId 삭제할 댓글
     */
    public void deleteReply(Long replyId) {
        deleteReportId(replyId);
        replyRepository.deleteById(replyId);
    }

    /**
     * 신고 테이블에서 삭제된 타겟에 대한 ID 삭제
     */
    private void deleteReportId(Long targetId) {
        reportRepository.deleteByTargetId(targetId);
    }


}
