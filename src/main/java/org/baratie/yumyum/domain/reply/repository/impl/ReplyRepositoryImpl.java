package org.baratie.yumyum.domain.reply.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.dto.MyReplyDto;
import org.baratie.yumyum.domain.reply.domain.Reply;
import org.baratie.yumyum.domain.reply.dto.ReplyResponseDto;
import org.baratie.yumyum.domain.reply.repository.ReplyCustomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static org.baratie.yumyum.domain.member.domain.QMember.member;
import static org.baratie.yumyum.domain.reply.domain.QReply.*;

@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Slice<ReplyResponseDto> getReplyOnReview(Long reviewId, Pageable pageable) {

        List<ReplyResponseDto> results = query.select(Projections.constructor(ReplyResponseDto.class,
                        reply.id,
                        member.imageUrl,
                        member.nickname,
                        reply.content,
                        reply.createdAt)
                )
                .from(reply)
                .leftJoin(reply.member, member)
                .where(reply.review.id.eq(reviewId))
                .orderBy(reply.createdAt.desc())
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
    public Slice<MyReplyDto> findByMemberId(Long memberId, Pageable pageable) {

        List<MyReplyDto> results = query.select(Projections.constructor(MyReplyDto.class,
                                reply.id,
                                member.nickname,
                                reply.content,
                                reply.createdAt,
                                reply.review.id)
                )
                .from(reply)
                .leftJoin(reply.member, member)
                .where(reply.member.id.eq(memberId))
                .orderBy(reply.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1)
                .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();

        if(hasNext){
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    @Override
    public List<Long> findByReviewId(Long reviewId) {
        return query.select(reply.id)
                .from(reply)
                .where(reply.review.id.eq(reviewId))
                .fetch();
    }

    @Override
    public Long findReviewByReplyId(Long replyId) {
        return query.select(reply.review.id)
                .from(reply)
                .where(replyIdEq(replyId))
                .fetchOne();
    }

    public BooleanExpression replyIdEq(Long replyId) {
        return reply.id.eq(replyId);
    }

}
