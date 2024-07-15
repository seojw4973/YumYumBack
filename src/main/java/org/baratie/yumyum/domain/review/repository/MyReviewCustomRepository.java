package org.baratie.yumyum.domain.review.repository;

import org.baratie.yumyum.domain.review.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface MyReviewCustomRepository {

    Long findMemberIdByReviewId(Long reviewId);

    Slice<LikeReviewDto> findLikeReviewsByMemberId(Long memberId, Pageable pageable);

    Slice<MyReviewDto> getMyReview(Long memberId, Pageable pageable);
}
