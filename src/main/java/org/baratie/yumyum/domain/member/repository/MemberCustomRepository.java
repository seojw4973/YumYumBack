package org.baratie.yumyum.domain.member.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberCustomRepository {

    boolean checkDeletedMember(Long memberId);
}
