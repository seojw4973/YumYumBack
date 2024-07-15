package org.baratie.yumyum.domain.store.repository;

import org.baratie.yumyum.domain.store.dto.SearchStoreDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchCustomRepository {
    List<SearchStoreDto> findSearchStore(Long memberId, String keyword);

    List<SearchStoreDto> findNearByStore(Double lng, Double lat);
}
