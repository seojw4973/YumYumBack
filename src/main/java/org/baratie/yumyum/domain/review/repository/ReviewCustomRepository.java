package org.baratie.yumyum.domain.review.repository;

import org.baratie.yumyum.domain.review.dto.ReviewAllDto;
import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;
import org.baratie.yumyum.domain.review.dto.StoreReviewDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCustomRepository {

    ReviewDetailDto findReviewDetail(Long memberId, Long reviewId);

    Slice<ReviewAllDto> findAllReviews(Pageable pageable);

    Long findMemberIdByReviewId(Long reviewId);

    Slice<StoreReviewDto> findReviewByStoreId(Long storeId, Pageable pageable);
}
