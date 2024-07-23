package org.baratie.yumyum.global.subquery;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import static org.baratie.yumyum.domain.member.domain.QMember.member;
import static org.baratie.yumyum.domain.review.domain.QReview.review;

public class TotalReviewCount {

    /**
     * 리뷰 총 갯수
     * @return 멤버가 작성한 리뷰 갯수
     */
    public static JPQLQuery<Long> getReviewTotalCount() {
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
    public static JPQLQuery<Long> getReviewTotalCount(Long memberId) {
        return JPAExpressions
                .select(review.count())
                .from(review)
                .where(memberIdEq(memberId));
    }

    private static BooleanExpression memberIdEq(Long memberId) {
        return review.member.id.eq(memberId);
    }

}
