package org.baratie.yumyum.domain.member.repository;

import org.baratie.yumyum.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    boolean existsByNickname(String nickname);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
