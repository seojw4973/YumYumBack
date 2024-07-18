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

    /**
     * 특정 가게 즐겨찾기 했는지 확인
     * @param memberId 로그인한 멤버 id
     * @param reviewId 즐겨찾기 누를 가게 id
     * @return BooleanExpression
     */
    private BooleanExpression memberIdReviewIdEq(Long memberId, Long reviewId) {
        return memberIdEq(memberId).and(reviewIdEq(reviewId));
    }

    /**
     * 로그인한 멤버가 즐겨찾기 눌렀는지 확인
     * @param memberId 로그인한 멤버 id
     * @return BooleanExpression
     */
    public BooleanExpression memberIdEq(Long memberId) {
        return likes.member.id.eq(memberId);
    }

    /**
     * 즐겨찾기 누른 가게가 있는지 확인
     * @param reviewId 즐겨찾기 누른 가게 id
     * @return
     */
    private BooleanExpression reviewIdEq(Long reviewId) {return likes.review.id.eq(reviewId);
    }
}
