package org.baratie.yumyum.domain.review.repository.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import org.baratie.yumyum.domain.review.dto.*;
import org.baratie.yumyum.domain.review.repository.ReviewCustomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static org.baratie.yumyum.domain.likes.domain.QLikes.likes;
import static org.baratie.yumyum.domain.member.domain.QMember.*;
import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.store.domain.QStore.store;

@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public ReviewDetailDto findReviewDetail(Long memberId, Long reviewId) {

        JPQLQuery<Boolean> likeStatus = JPAExpressions.select(likes.isLikes)
                .from(likes)
                .where(likes.review.id.eq(reviewId), likes.member.id.eq(memberId));

        return query.select(
                        Projections.constructor(ReviewDetailDto.class,
                                review.id,
                                member.id,
                                member.imageUrl.as("profileImage"),
                                member.nickname,
                                ExpressionUtils.as(getReviewTotalCount(memberId), "totalReviewCount"),
                                ExpressionUtils.as(getAvgGrade(memberId), "avgGrade"),
                                store.name.as("storeName"),
                                store.address,
                                review.grade,
                                review.content,
                                likeStatus
                        )
                )
                .from(review)
                .leftJoin(review.member, member)
                .where(reviewIdEq(reviewId).and(memberIdEq(memberId)))
                .fetchOne();
    }

    @Override
    public Slice<ReviewAllDto> findAllReviews(Pageable pageable) {

        /**
         * 무한스크롤로 리뷰 전체리스트 조회
         */
        List<ReviewAllDto> results = query
                .select(Projections.fields(ReviewAllDto.class,
                        review.id.as("reviewId"),
                        member.imageUrl.as("profileImage"),
                        store.name.as("storeName"),
                        store.address.as("address"),
                        member.nickname.as("nickname"),
                        review.grade,
                        ExpressionUtils.as(getReviewTotalCount(), "totalReviewCount"),
                        ExpressionUtils.as(getAvgGrade(), "avgGrade"),
                        review.content)
                )
                .from(review)
                .leftJoin(review.store, store)
                .leftJoin(review.member, member)
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();

        if (hasNext) {
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    @Override
    public Slice<StoreReviewDto> findReviewByStoreId(Long storeId, Pageable pageable) {

        List<StoreReviewDto> results = query.select(Projections.constructor(StoreReviewDto.class,
                        member.imageUrl,
                        member.nickname,
                        review.grade,
                        ExpressionUtils.as(getReviewTotalCount(), "totalReviewCount"),
                        ExpressionUtils.as(getAvgGrade(), "avgGrade"),
                        review.content
                ))
                .from(review)
                .leftJoin(review.member, member)
                .where(storeIdEq(storeId))
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
     * 내가 작성한 리뷰
     * @param memberId 로그인한 사용자 ID
     * @return 로그인한 사용자가 작성한 리뷰
     */
    @Override
    public Slice<MyReviewDto> getMyReview(Long memberId, Pageable pageable) {
        List<MyReviewDto> results = query.select(Projections.constructor(MyReviewDto.class,
                        review.id,
                        store.name,
                        store.address,
                        member.nickname,
                        review.grade,
                        getReviewTotalCount(memberId),
                        getAvgGrade(memberId),
                        review.content,
                        likes.isLikes
                ))
                .from(review)
                .leftJoin(review.member, member)
                .leftJoin(review.store, store)
                .leftJoin(likes).on(likes.review.id.eq(review.id))
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .where(memberIdEq(memberId))
                .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    @Override
    public Slice<LikeReviewDto> findLikeReviewsByMemberId(Long memberId, Pageable pageable) {
        List<LikeReviewDto> results = query.select(Projections.constructor(LikeReviewDto.class,
                review.id,
                member.imageUrl.as("profileImage"),
                member.nickname,
                review.grade,
                likes.isLikes,
                review.content,
                review.createdAt))
                .from(review)
                .leftJoin(review.member, member)
                .leftJoin(likes).on(likes.review.id.eq(review.id))
                .where(likesMemberIdEq(memberId).and(likes.isLikes.eq(true)))
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() +1)
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
     * 리뷰 총 갯수
     * @return 멤버가 작성한 리뷰 갯수
     */
    private JPQLQuery<Long> getReviewTotalCount() {
        return JPAExpressions
                .select(review.count())
                .from(review)
                .where(review.member.id.eq(member.id));
    }

    /**
     * 리뷰 총 갯수
     * @param memberId 조회할 멤버
     * @return 멤버가 작성한 리뷰 갯수
     */
    private JPQLQuery<Long> getReviewTotalCount(Long memberId) {
        return JPAExpressions
                .select(review.count())
                .from(review)
                .where(memberIdEq(memberId));
    }

    /**
     * 리뷰 총 갯수
     * @return 멤버가 작성한 평균 리뷰 점수
     */
    private JPQLQuery<Double> getAvgGrade() {

        NumberTemplate<Double> roundedAvgGrade = Expressions.numberTemplate(Double.class, "ROUND({0}, 2)", review.grade.avg());
        return JPAExpressions
                .select(roundedAvgGrade)
                .from(review)
                .where(review.member.id.eq(member.id));
    }

    /**
     * 평균 별점
     * @param memberId 조회할 멤버
     * @return 멤버가 작성한 평균 리뷰 점수
     */
    private JPQLQuery<Double> getAvgGrade(Long memberId) {
        return JPAExpressions
                .select(review.grade.avg())
                .from(review)
                .where(memberIdEq(memberId));
    }

    public BooleanExpression memberIdEq(Long memberId) {
        return review.member.id.eq(memberId);
    }

    public BooleanExpression reviewIdEq(Long reviewId) {
        return review.id.eq(reviewId);
    }

    public BooleanExpression storeIdEq(Long storeId) {
        return review.store.id.eq(storeId);
    }
  
    public BooleanExpression likesMemberIdEq(Long memberId) { 
      return likes.member.id.eq(memberId); 
   }

}
