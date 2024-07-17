package org.baratie.yumyum.domain.review.repository;

import org.baratie.yumyum.domain.review.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MyReviewCustomRepository {

    Long findMemberIdByReviewId(Long reviewId);

    Slice<LikeReviewDto> findLikeReviewsByMemberId(Long memberId, Pageable pageable);

    Slice<MyReviewDto> getMyReview(Long memberId, Map<Long, List<String>> imageMap, Pageable pageable);
}
