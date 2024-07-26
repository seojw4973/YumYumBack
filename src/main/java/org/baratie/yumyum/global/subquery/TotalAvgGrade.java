package org.baratie.yumyum.global.subquery;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.baratie.yumyum.domain.store.domain.QStore;

import static org.baratie.yumyum.domain.member.domain.QMember.member;
import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.store.domain.QStore.store;

public class TotalAvgGrade {

    /**
     * 평균 별점
     * @return 멤버가 작성한 평균 리뷰 점수
     */
    public static JPQLQuery<Double> getAvgGradeWithMember() {
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
    public static JPQLQuery<Double> getAvgGradeWithMember(Long memberId) {
        return JPAExpressions
                .select(review.grade.avg())
                .from(review)
                .where(memberIdEq(memberId));
    }

    /**
     * 평균 별점
     * @return 가게 평균 리뷰 점수
     */
    public static JPQLQuery<Double> getAvgGradeWithStore() {
        return JPAExpressions
                .select(review.grade.avg())
                .from(review)
                .where(review.store.id.eq(store.id));
    }

    public static BooleanExpression memberIdEq(Long memberId) {
        return review.id.eq(memberId);
    }

}
