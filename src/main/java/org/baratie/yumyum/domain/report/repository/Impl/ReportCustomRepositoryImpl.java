package org.baratie.yumyum.domain.report.repository.Impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.report.domain.ReportType;
import org.baratie.yumyum.domain.report.dto.ReportPageResponseDto;
import org.baratie.yumyum.domain.report.repository.ReportCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.baratie.yumyum.domain.member.domain.QMember.member;
import static org.baratie.yumyum.domain.reply.domain.QReply.reply;
import static org.baratie.yumyum.domain.review.domain.QReview.review;
import static org.baratie.yumyum.domain.report.domain.QReport.report;

@RequiredArgsConstructor
public class ReportCustomRepositoryImpl implements ReportCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<ReportPageResponseDto> findByReviewType(Pageable pageable) {
        List<ReportPageResponseDto> results = query.select(Projections.constructor(ReportPageResponseDto.class,
                report.id,
                report.member.nickname,
                review.content,
                review.id,
                report.content,
                report.createdAt))
                .from(report)
                .leftJoin(review).on(report.targetId.eq(review.id))
                .leftJoin(report.member, member)
                .where(report.type.eq(ReportType.REVIEW))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(report.createdAt.desc())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

    @Override
    public Page<ReportPageResponseDto> findByReplyType(Pageable pageable) {
        List<ReportPageResponseDto> results = query.select(Projections.constructor(ReportPageResponseDto.class,
                        report.id,
                        report.member.nickname,
                        reply.content,
                        reply.id,
                        report.content,
                        report.createdAt))
                .from(report)
                .leftJoin(reply).on(report.targetId.eq(reply.id))
                .leftJoin(report.member, member)
                .where(report.type.eq(ReportType.REPLY))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(report.createdAt.desc())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }
}
