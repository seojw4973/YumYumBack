package org.baratie.yumyum.domain.review.repository;

import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCustomRepository {

    ReviewDetailDto findReviewDetail(Long storeId, Long reviewId);
}
