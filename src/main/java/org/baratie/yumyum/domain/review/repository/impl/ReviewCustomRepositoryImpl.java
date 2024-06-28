package org.baratie.yumyum.domain.review.repository.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;

import org.baratie.yumyum.domain.review.repository.ReviewCustomRepository;

import static org.baratie.yumyum.domain.member.domain.QMember.*;
import static org.baratie.yumyum.domain.review.domain.QReview.*;
import static org.baratie.yumyum.domain.store.domain.QStore.store;

@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public ReviewDetailDto findReviewDetail(Long memberId, Long reviewId) {

        /**
         * 멤버가 작성한 전체 리뷰 갯수
         */
        JPQLQuery<Long> reviewTotalCount = JPAExpressions
                .select(review.count())
                .from(review)
                .where(memberIdEq(memberId));
        /**
         * 멤버가 작성한 전체 리뷰 평균
         */
        JPQLQuery<Double> avgGrade = JPAExpressions
                .select(review.grade.avg())
                .from(review)
                .where(memberIdEq(memberId));

        ReviewDetailDto reviewDetailDto = query
                .select(Projections.constructor(ReviewDetailDto.class,
                        member.imageUrl.as("profileImage"),
                        member.nickname,
                        ExpressionUtils.as(reviewTotalCount, "totalReviewCount"),
                        ExpressionUtils.as(avgGrade, "avgGrade"),
                        store.name.as("storeName"),
                        store.address,
                        review.content))
                .from(review)
                .join(review.member, member)
                .join(review.store, store)
                .where(reviewIdEq(reviewId).and(memberIdEq(memberId)))
                .fetchOne();

        return reviewDetailDto;
    }


    /**
     * 리뷰를 작성한 멤버만 조회
     * @param memberId 특정 멤버 조회
     * @return
     */
    public BooleanExpression memberIdEq(Long memberId) {
        return review.member.id.eq(memberId);
    }

    /**
     * @param reviewId 특정 리뷰 조회
     * @return
     */
    public BooleanExpression reviewIdEq(Long reviewId) {
        return review.id.eq(reviewId);
    }

}
