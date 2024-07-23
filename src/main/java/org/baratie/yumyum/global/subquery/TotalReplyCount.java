package org.baratie.yumyum.global.subquery;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import static org.baratie.yumyum.domain.reply.domain.QReply.reply;

public class TotalReplyCount {

    public static JPQLQuery<Long> getTotalReplyCount(Long memberId) {
        return JPAExpressions
                .select(reply.count())
                .from(reply)
                .where(reply.member.id.eq(memberId));
    }

}
