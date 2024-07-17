package org.baratie.yumyum.domain.store.repository;

import org.baratie.yumyum.domain.store.dto.SearchStoreDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SearchCustomRepository {
    List<SearchStoreDto> findSearchStore(Long memberId, Map<Long, List<String>> imageList, Map<Long, List<String>> hashtagList, String keyword);

    List<SearchStoreDto> findNearByStore(Double lng, Double lat, Map<Long, List<String>> imageList, Map<Long, List<String>> hashtagList);
}
