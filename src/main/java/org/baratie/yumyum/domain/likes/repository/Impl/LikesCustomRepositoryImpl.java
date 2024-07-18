package org.baratie.yumyum.domain.likes.repository.Impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.likes.domain.Likes;
import org.baratie.yumyum.domain.likes.repository.LikesCustomRepository;

import java.util.Optional;

import static org.baratie.yumyum.domain.likes.domain.QLikes.*;

@RequiredArgsConstructor
public class LikesCustomRepositoryImpl implements LikesCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<Likes> exist(Long memberId, Long reviewId) {
        return Optional.ofNullable(
                query.selectFrom(likes)
                        .where(memberIdReviewIdEq(memberId, reviewId))
                        .fetchOne()
        );
    }

    private BooleanExpression memberIdReviewIdEq(Long memberId, Long reviewId) {
        return memberIdEq(memberId).and(reviewIdEq(reviewId));}

    public BooleanExpression memberIdEq(Long memberId) {
        return likes.member.id.eq(memberId);
    }

    private BooleanExpression reviewIdEq(Long reviewId) {
        return likes.review.id.eq(reviewId);
    }
}
