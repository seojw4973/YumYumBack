package org.baratie.yumyum.domain.review.repository;

import org.baratie.yumyum.domain.review.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCustomRepository {

    ReviewDetailDto findReviewDetail(Long memberId, Long reviewId);

    Slice<ReviewAllDto> findAllReviews(Pageable pageable);

    Long findMemberIdByReviewId(Long reviewId);

    Slice<StoreReviewDto> findReviewByStoreId(Long storeId, Pageable pageable);

    Slice<LikeReviewDto> findLikeReviewsByMemberId(Long memberId, Pageable pageable);

    Slice<MyReviewDto> getMyReview(Long memberId, Pageable pageable);
}
