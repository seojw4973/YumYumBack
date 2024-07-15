package org.baratie.yumyum.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.dto.SimpleMemberDto;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.AdminStoreDto;
import org.baratie.yumyum.domain.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final MemberService memberService;

    /**
     * 관리자페이지 회원 조회
     */
    public Page<SimpleMemberDto> getSimpleMemberInfo(Pageable pageable) {
        return memberRepository.getSimpleMemberInfo(pageable);
    }

    /**
     * 관리자 페이지 맛집 전체 조회
     * @param pageable
     * @return 맛집 전체 데이터 Page 정보와 함께 리턴
     */
    public Page<AdminStoreDto> getAdminStores(Pageable pageable) {
        Page<Store> pageStore = storeRepository.findAll(pageable);
        return pageStore.map(m -> new AdminStoreDto(m.getId(), m.getName(), m.getCall(), m.getAddress(), m.isClosed()));
    }

    /**
     * 관리자 회원 탈퇴
     * @param memberId
     */
    public void deleteMember(Long memberId) {
        Member member = memberService.getMember(memberId);
        Member deletedMember = member.deleteMember(memberId);

        memberRepository.save(deletedMember);
    }
}
