package org.baratie.yumyum.domain.review.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import org.baratie.yumyum.domain.review.dto.*;
import org.baratie.yumyum.domain.review.repository.MyReviewCustomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static org.baratie.yumyum.domain.likes.domain.QLikes.likes;
import static org.baratie.yumyum.domain.member.domain.QMember.*;
import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.store.domain.QStore.store;
import static org.baratie.yumyum.global.utils.file.domain.QImage.image;

@RequiredArgsConstructor
public class MyReviewCustomRepositoryImpl implements MyReviewCustomRepository {

    private final JPAQueryFactory query;



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

    /**
     * 좋아요한 리뷰 보기
     * @param memberId 로그인한 유저 id 값
     * @param pageable
     * @return 내가 좋아요한 리뷰 리스트 리턴
     */
    @Override
    public Slice<LikeReviewDto> findLikeReviewsByMemberId(Long memberId, Pageable pageable) {

        List<LikeReviewDto> results = query.select(Projections.constructor(LikeReviewDto.class,
                        review.id,
                        store.name,
                        store.address,
                        member.nickname,
                        review.grade,
                        getReviewTotalCount(),
                        getAvgGrade(),
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

        for(LikeReviewDto dto : results){
            List<String> images = query.select(
                            image.imageUrl)
                    .from(image)
                    .where(image.review.id.eq(dto.getReviewId()))
                    .fetch();
            dto.addImageList(images);
        }

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
        return JPAExpressions
                .select(review.grade.avg())
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
