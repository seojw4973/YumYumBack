package org.baratie.yumyum.domain.member.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.QMember;
import org.baratie.yumyum.domain.member.dto.MemberBasicDto;
import org.baratie.yumyum.domain.member.dto.SimpleMemberDto;
import org.baratie.yumyum.domain.member.repository.MemberCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.baratie.yumyum.domain.member.domain.QMember.*;
import static org.baratie.yumyum.domain.review.domain.QReview.review;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public boolean checkDeletedMember(Long memberId) {
        return query.select(member.isDeleted)
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne();
    }

    @Override
    public MemberBasicDto findMemberBasisInfo(Long memberId) {

        return query.select(
                        Projections.constructor(MemberBasicDto.class,
                                member.id,
                                member.imageUrl.as("profileImage"),
                                member.nickname,
                                ExpressionUtils.as(getReviewTotalCount(memberId), "totalReviewCount"),
                                ExpressionUtils.as(getAvgGrade(memberId), "avgGrade"))
                ).from(member)
                .where(memberIdEq(memberId))
                .fetchOne();
    }

    @Override
    public Page<SimpleMemberDto> getSimpleMemberInfo(Pageable pageable) {
        List<SimpleMemberDto> results = query.select(Projections.constructor(SimpleMemberDto.class,
                        member.nickname,
                        member.email,
                        member.phoneNumber,
                        member.isDeleted
                ))
                .from(member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.nickname.asc(), member.id.asc())
                .fetch();



        return new PageImpl<>(results, pageable, results.size());

    }

    @Override
    public Member findByIdNotDeleted(Long memberId) {
        return query.selectFrom(member)
                .where(member.id.eq(memberId).and(member.isDeleted.eq(false)))
                .fetchOne();
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
        return member.id.eq(memberId);
    }

}
