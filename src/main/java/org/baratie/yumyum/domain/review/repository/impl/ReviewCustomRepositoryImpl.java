package org.baratie.yumyum.domain.review.repository.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.review.dto.ReviewAllDto;
import org.baratie.yumyum.domain.review.dto.ReviewDetailDto;
import org.baratie.yumyum.domain.review.dto.StoreReviewDto;
import org.baratie.yumyum.domain.review.repository.ReviewCustomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import java.util.List;
import java.util.Map;

import static org.baratie.yumyum.domain.likes.domain.QLikes.likes;
import static org.baratie.yumyum.domain.member.domain.QMember.member;
import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.store.domain.QStore.store;
import static org.baratie.yumyum.global.subquery.TotalAvgGrade.getAvgGrade;
import static org.baratie.yumyum.global.subquery.TotalReviewCount.getReviewTotalCount;

@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory query;

    /**
     * 리뷰 상세 조회
     * @param memberId 좋아요 상태 구분하기 위해 리뷰
     * @param reviewId 상세 조회한 리뷰 id
     * @return
     */
    @Override
    public ReviewDetailDto findReviewDetail(Long memberId, Long reviewId) {

        JPQLQuery<Boolean> likeStatus = JPAExpressions.select(likes.isLikes)
                .from(likes)
                .where(likes.review.id.eq(reviewId), likes.member.id.eq(memberId));

        return query.select(
                        Projections.constructor(ReviewDetailDto.class,
                                member.id,
                                member.imageUrl,
                                member.nickname,
                                ExpressionUtils.as(getReviewTotalCount(), "totalReviewCount"),
                                ExpressionUtils.as(getAvgGrade(), "avgGrade"),
                                review.id,
                                review.grade,
                                review.content,
                                review.createdAt,
                                store.name.as("storeName"),
                                store.address,
                                likeStatus
                        ))
                .from(review)
                .leftJoin(review.store, store)
                .leftJoin(review.member, member)
                .where(reviewIdEq(reviewId))
                .fetchOne();
    }

    /**
     * 리뷰 전체 리스트 조회
     * @param pageable
     * @return 최신 작성순으로 리뷰 전체 리스트 리턴
     */
    @Override
    public Slice<ReviewAllDto> findAllReviews(Long memberId, Map<Long, List<String>> imageMap, Pageable pageable) {

        JPQLQuery<Boolean> likeStatus = JPAExpressions.select(likes.isLikes)
                .from(likes)
                .where(likes.review.id.eq(review.id), likes.member.id.eq(memberId));

        List<ReviewAllDto> results =
                query.select(Projections.constructor(ReviewAllDto.class,
                                review.id.as("reviewId"),
                                member.imageUrl.as("profileImage"),
                                store.name.as("storeName"),
                                store.address.as("address"),
                                member.nickname.as("nickname"),
                                review.grade,
                                ExpressionUtils.as(getReviewTotalCount(), "totalReviewCount"),
                                ExpressionUtils.as(getAvgGrade(), "avgGrade"),
                                review.content,
                                likeStatus)
                        )
                        .from(review)
                        .leftJoin(review.store, store)
                        .leftJoin(review.member, member)
                        .orderBy(review.createdAt.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize() + 1)
                        .fetch();

        results.forEach(dto -> {
            List<String> images = imageMap.get(dto.getReviewId());
            if (images != null) {
                dto.addImageList(images);
            }
        });

        boolean hasNext = results.size() > pageable.getPageSize();

        if (hasNext) {
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    /**
     * 맛집에 작성된 리뷰 조회
     * @param storeId 상세페이지 조회한 가게 id 값
     * @param pageable
     * @return 맛집에 작성된 리뷰 리스트 리턴
     */
    @Override
    public Slice<StoreReviewDto> findReviewByStoreId(Long memberId, Long storeId, Map<Long, List<String>> imageList, Pageable pageable) {

        JPQLQuery<Boolean> likeStatus = JPAExpressions.select(likes.isLikes)
                .from(likes)
                .where(likes.review.id.eq(review.id), likes.member.id.eq(memberId));

        List<StoreReviewDto> results =
                query.select(Projections.constructor(StoreReviewDto.class,
                                member.id,
                                member.imageUrl,
                                member.nickname,
                                review.id,
                                review.grade,
                                review.content,
                                ExpressionUtils.as(getReviewTotalCount(), "totalReviewCount"),
                                ExpressionUtils.as(getAvgGrade(), "avgGrade"),
                                likeStatus
                        ))
                        .from(review)
                        .leftJoin(review.member, member)
                        .where(storeIdEq(storeId))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize() + 1)
                        .orderBy(review.createdAt.desc())
                        .fetch();

        results.forEach(dto -> {
            List<String> images = imageList.get(dto.getReviewId());
            if (images != null) {
                dto.addImage(images);
            }
        });

        boolean hasNext = results.size() > pageable.getPageSize();

        if (hasNext) {
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }



    public BooleanExpression reviewIdEq(Long reviewId) {
        return review.id.eq(reviewId);
    }

    public BooleanExpression storeIdEq(Long storeId) {
        return review.store.id.eq(storeId);
    }
}
