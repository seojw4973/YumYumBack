package org.baratie.yumyum.global.subquery;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import static org.baratie.yumyum.domain.likes.domain.QLikes.*;

public class LikeReviewCount {
    public static JPQLQuery<Long> getLikeReviewCount(Long memberId) {
        return JPAExpressions
                .select(likes.count())
                .from(likes)
                .where(memberIdEq(memberId), likes.isLikes.eq(true));
    }

    public static BooleanExpression memberIdEq(Long memberId) {
        return likes.member.id.eq(memberId);
    }

}
