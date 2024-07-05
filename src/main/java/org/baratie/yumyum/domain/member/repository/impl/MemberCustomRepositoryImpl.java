package org.baratie.yumyum.domain.member.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.QMember;
import org.baratie.yumyum.domain.member.repository.MemberCustomRepository;

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
}
