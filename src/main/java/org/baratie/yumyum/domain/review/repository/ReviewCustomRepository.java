package org.baratie.yumyum.domain.review.repository;

import org.baratie.yumyum.domain.review.dto.ReviewAllDto;
import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;
import org.baratie.yumyum.domain.review.dto.StoreReviewDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReviewCustomRepository {

    Slice<ReviewAllDto> findAllReviews(Pageable pageable);

    Slice<StoreReviewDto> findReviewByStoreId(Long storeId, Map<Long, List<String>> imageList, Pageable pageable);

    ReviewDetailDto findReviewDetail(Long memberId, Long reviewId);
}
