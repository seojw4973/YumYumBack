package org.baratie.yumyum.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.hashtag.repository.HashtagRepository;
import org.baratie.yumyum.domain.store.dto.SearchStoreDto;
import org.baratie.yumyum.domain.store.repository.StoreRepository;
import org.baratie.yumyum.global.utils.file.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final StoreRepository storeRepository;
    private final ImageRepository imageRepository;
    private final HashtagRepository hashtagRepository;

    /**
     * 검색 시 맛집 리스트 조회
     * @param memberId 로그인한 유저 id값
     * @param keyword 검색어
     * @return 검색 조건에 맞는 맛집 리스트 30개 제한으로 리턴
     */
    public List<SearchStoreDto> getSearchStores(Long memberId, String keyword) {
        Map<Long, String> imageMap = imageRepository.findImageByStoreIdList();
        Map<Long, List<String>> hashtagMap = hashtagRepository.findHashtagByStoreId();
        return storeRepository.findSearchStore(memberId, imageMap, hashtagMap, keyword);
    }

    /**
     * 기본 위치 기준 근처 맛집 조회
     * @param lng 경도
     * @param lat 위도
     * @return 전달받은 위치 반경 1km 내의 맛집 리스트 리턴
     */
    public List<SearchStoreDto> getNearByStore(Double lng, Double lat, Long memberId) {
        Map<Long, String> imageMap = imageRepository.findImageByStoreIdList();
        Map<Long, List<String>> hashtagMap = hashtagRepository.findHashtagByStoreId();
        return storeRepository.findNearByStore(memberId ,lng, lat, imageMap, hashtagMap);
    }
}
