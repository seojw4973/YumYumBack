package org.baratie.yumyum.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.dto.MyReplyDto;
import org.baratie.yumyum.domain.reply.repository.ReplyRepository;
import org.baratie.yumyum.domain.review.dto.LikeReviewDto;
import org.baratie.yumyum.domain.review.dto.MyReviewDto;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.domain.store.dto.MyFavoriteStoreDto;
import org.baratie.yumyum.domain.store.repository.StoreRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final ReplyRepository replyRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    /**
     * 내가 쓴 댓글 보기
     * @param memberId
     * @param pageable
     * @return Slice객체로 내가 쓴 댓글 리턴
     */
    public Slice<MyReplyDto> getMyReply(Long memberId, Pageable pageable) {
        return replyRepository.findByMemberId(memberId, pageable);
    }

    /**
     * 좋아요한 리뷰 조회
     * @param memberId
     * @param pageable
     * @return 좋아요한 리뷰 리스트 리턴
     */
    public Slice<LikeReviewDto> getMyLikeReview(Long memberId, Pageable pageable) {
        return reviewRepository.findLikeReviewsByMemberId(memberId, pageable);
    }

    /**
     * 내가 쓴 리뷰
     */
    public Slice<MyReviewDto> getMyReview(Long memberId, Pageable pageable) {
        return reviewRepository.getMyReview(memberId, pageable);
    }

    /**
     * 내가 즐겨찾기 한 맛집 조회
     * @param memberId
     */
    public Slice<MyFavoriteStoreDto> getMyFavoriteStore(Long memberId, Pageable pageable) {
        return storeRepository.findFavoriteStore(memberId, pageable);
    }
}
