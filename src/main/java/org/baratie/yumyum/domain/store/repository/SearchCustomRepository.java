package org.baratie.yumyum.domain.store.repository;

import org.baratie.yumyum.domain.store.dto.SearchStoreDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SearchCustomRepository {
    List<SearchStoreDto> findSearchStore(Long memberId, Map<Long, String> imageMap, Map<Long, List<String>> hashtagMap, String keyword);

    List<SearchStoreDto> findNearByStore(Long memberId, Double lng, Double lat, Map<Long, String> imageMap, Map<Long, List<String>> hashtagMap);
}
