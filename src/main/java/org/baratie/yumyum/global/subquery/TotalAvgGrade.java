package org.baratie.yumyum.global.subquery;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import static org.baratie.yumyum.domain.member.domain.QMember.member;
import static org.baratie.yumyum.domain.review.domain.QReview.review;

public class TotalAvgGrade {

    /**
     * 평균 별점
     * @return 멤버가 작성한 평균 리뷰 점수
     */
    public static JPQLQuery<Double> getAvgGrade() {
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
    public static JPQLQuery<Double> getAvgGrade(Long memberId) {
        return JPAExpressions
                .select(review.grade.avg())
                .from(review)
                .where(memberIdEq(memberId));
    }

    public static BooleanExpression memberIdEq(Long memberId) {
        return member.id.eq(memberId);
    }

}
