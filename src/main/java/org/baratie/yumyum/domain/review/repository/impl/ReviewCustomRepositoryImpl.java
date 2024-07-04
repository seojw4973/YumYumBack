package org.baratie.yumyum.domain.review.repository.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.image.domain.QImage;
import org.baratie.yumyum.domain.image.dto.ImageDto;
import org.baratie.yumyum.domain.member.domain.QMember;
import org.baratie.yumyum.domain.review.domain.QReview;
import org.baratie.yumyum.domain.review.dto.ReviewAllDto;
import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;

import org.baratie.yumyum.domain.review.repository.ReviewCustomRepository;
import org.baratie.yumyum.domain.store.domain.QStore;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.stream.Collectors;

import static org.baratie.yumyum.domain.member.domain.QMember.*;
import static org.baratie.yumyum.domain.review.domain.QReview.*;
import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.store.domain.QStore.*;
import static org.baratie.yumyum.domain.store.domain.QStore.store;

@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public ReviewDetailDto findReviewDetail(Long memberId, Long reviewId) {

        /**
         * 멤버가 작성한 리뷰 갯수
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
                .select(
                        Projections.constructor(ReviewDetailDto.class,
                        member.imageUrl.as("profileImage"),
                        member.nickname,
                        ExpressionUtils.as(reviewTotalCount, "totalReviewCount"),
                        ExpressionUtils.as(avgGrade, "avgGrade"),
                        store.name.as("storeName"),
                        store.address,
                        review.grade,
                        review.content)
                )
                .from(review)
                .leftJoin(review.member, member)
                .where(reviewIdEq(reviewId).and(memberIdEq(memberId)))
                .fetchOne();

        return reviewDetailDto;
    }

    @Override
    public Slice<ReviewAllDto> findAllReviews(Pageable pageable) {

        /**
         * 멤버가 작성한 리뷰 갯수
         */
        JPQLQuery<Long> reviewCount = JPAExpressions
                .select(review.count())
                .from(review)
                .where(review.member.id.eq(member.id));


        /**
         * 멤버의 평균 평점
         */
        JPQLQuery<Double> avgGrade = JPAExpressions
                .select(review.grade.avg())
                .from(review)
                .where(review.member.id.eq(member.id));

        /**
         * 무한스크롤로 리뷰 전체리스트 조회
         */
        List<ReviewAllDto> results = query
                .select(Projections.constructor(ReviewAllDto.class,
                        store.name,
                        store.address,
                        member.nickname,
                        review.grade,
                        ExpressionUtils.as(reviewCount, "reviewCount"),
                        ExpressionUtils.as(avgGrade, "avgGrade"),
                        review.content)
                )
                .from(review)
                .leftJoin(review.store, store)
                .leftJoin(review.member, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();

        if (hasNext) {
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    /**
     * 멤버 ID 조회
     * @param reviewId
     * @return 리뷰 작성자의 ID값
     */
    @Override
    public Long findMemberIdByReviewId(Long reviewId) {
        return query.select(review.member.id)
                .from(review)
                .where(reviewIdEq(reviewId))
                .fetchOne();
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
