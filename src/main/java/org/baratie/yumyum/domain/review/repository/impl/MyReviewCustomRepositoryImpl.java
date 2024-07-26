package org.baratie.yumyum.domain.review.repository.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import org.baratie.yumyum.domain.review.dto.*;
import org.baratie.yumyum.domain.review.repository.MyReviewCustomRepository;
import org.baratie.yumyum.global.subquery.TotalAvgGrade;
import org.baratie.yumyum.global.subquery.TotalReviewCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Map;

import static org.baratie.yumyum.domain.likes.domain.QLikes.likes;
import static org.baratie.yumyum.domain.member.domain.QMember.*;
import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.store.domain.QStore.store;
import static org.baratie.yumyum.global.subquery.LikeReviewCount.getLikeReviewCount;
import static org.baratie.yumyum.global.subquery.ReplyCount.getReplyCount;
import static org.baratie.yumyum.global.subquery.TotalAvgGrade.getAvgGradeWithMember;


@RequiredArgsConstructor
public class MyReviewCustomRepositoryImpl implements MyReviewCustomRepository {

    private final JPAQueryFactory query;

    /**
     * 내가 작성한 리뷰
     * @param memberId 로그인한 사용자 ID
     * @return 로그인한 사용자가 작성한 리뷰
     */
    @Override
    public Slice<MyReviewDto> getMyReview(Long memberId, Map<Long, List<String>> imageMap, Pageable pageable) {
        List<MyReviewDto> results =
                query.select(Projections.constructor(MyReviewDto.class,
                                review.id,
                                store.name,
                                store.address,
                                member.nickname,
                                member.imageUrl,
                                review.grade,
                                ExpressionUtils.as(TotalReviewCount.getReviewTotalCountWithMember(memberId), "totalReviewCount"),
                                ExpressionUtils.as(TotalAvgGrade.getAvgGradeWithMember(memberId), "avgGrade"),
                                ExpressionUtils.as(getReplyCount(), "replyCount"),
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

        results.forEach(dto -> {
            List<String> imageList = imageMap.get(dto.getReviewId());

            if (imageList != null) {
                dto.addImageList(imageList);
            }
        });

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    /**
     * 좋아요한 리뷰 보기
     * @param memberId 로그인한 유저 id 값
     * @param pageable
     * @return 내가 좋아요한 리뷰 리스트 리턴
     */
    @Override
    public Slice<LikeReviewDto> findLikeReviewsByMemberId(Long memberId, Map<Long, List<String>> imageMap, Pageable pageable) {

        List<LikeReviewDto> results =
                query.select(Projections.constructor(LikeReviewDto.class,
                                review.id,
                                store.name,
                                store.address,
                                member.nickname,
                                member.imageUrl,
                                review.grade,
                                ExpressionUtils.as(getLikeReviewCount(memberId), "likeReviewCount"),
                                ExpressionUtils.as(getAvgGradeWithMember(), "avgGrade"),
                                ExpressionUtils.as(getReplyCount(), "replyCount"),
                                review.content,
                                likes.isLikes
                        ))
                        .from(review)
                        .leftJoin(review.member, member)
                        .leftJoin(review.store, store)
                        .leftJoin(likes).on(likes.review.id.eq(review.id))
                        .where(likes.member.id.eq(memberId).and(likes.isLikes.eq(true)))
                        .orderBy(review.createdAt.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize() + 1)
                        .fetch();

        results.forEach(
                dto -> {
                    List<String> images = imageMap.get(dto.getReviewId());
                    dto.addImageList(images);
                }
        );

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

    public BooleanExpression memberIdEq(Long memberId) {
        return review.member.id.eq(memberId);
    }

    public BooleanExpression reviewIdEq(Long reviewId) {
        return review.id.eq(reviewId);
    }


}
