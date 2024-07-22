package org.baratie.yumyum.domain.likes.repository;

import org.baratie.yumyum.domain.likes.domain.Likes;

import java.util.Optional;

public interface LikesCustomRepository {
    Optional<Likes> exist(Long memberId, Long reviewId);

    void deleteByReviewId(Long reviewId);

}
