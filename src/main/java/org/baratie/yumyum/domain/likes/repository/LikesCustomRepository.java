package org.baratie.yumyum.domain.likes.repository;

import org.baratie.yumyum.domain.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface LikesCustomRepository {

    Optional<Likes> exist(Long memberId, Long reviewId);
}
