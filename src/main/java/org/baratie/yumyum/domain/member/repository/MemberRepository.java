package org.baratie.yumyum.domain.member.repository;

import org.baratie.yumyum.domain.category.domain.Category;
import org.baratie.yumyum.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
