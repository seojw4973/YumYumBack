package org.baratie.yumyum.global.subquery;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import static org.baratie.yumyum.domain.reply.domain.QReply.*;
import static org.baratie.yumyum.domain.review.domain.QReview.*;

public class ReplyCount {
    public static JPQLQuery<Long> getReplyCount() {
        return JPAExpressions
                .select(reply.count())
                .from(reply)
                .where(reply.review.id.eq(review.id));
    }
}
