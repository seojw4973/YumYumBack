package org.baratie.yumyum.domain.member.repository;

import org.baratie.yumyum.domain.member.dto.MemberBasicDto;
import org.baratie.yumyum.domain.member.dto.SimpleMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.baratie.yumyum.domain.member.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberCustomRepository {

    boolean checkDeletedMember(Long memberId);

    Page<SimpleMemberDto> getSimpleMemberInfo(Pageable pageable);

    Member findByIdNotDeleted(Long memberId);

}
