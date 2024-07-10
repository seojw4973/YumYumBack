package org.baratie.yumyum.domain.member.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.QMember;
import org.baratie.yumyum.domain.member.dto.SimpleMemberDto;
import org.baratie.yumyum.domain.member.repository.MemberCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.baratie.yumyum.domain.member.domain.QMember.*;

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
}
