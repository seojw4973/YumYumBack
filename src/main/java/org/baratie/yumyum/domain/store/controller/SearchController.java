package org.baratie.yumyum.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.store.dto.SearchStoreDto;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final StoreService storeService;

    /**
     * 내 주변 맛집
     */
    @GetMapping
    public ResponseEntity<List<SearchStoreDto>> findNearbyStore(@RequestParam Double lng, @RequestParam Double lat) {
        List<SearchStoreDto> nearByStoreList = storeService.getNearByStore(lng, lat);
        return ResponseEntity.status(HttpStatus.OK).body(nearByStoreList);
    }

    /**
     * 검색한 가게 리스트
     */
    @GetMapping("/search/{filter}")
    public ResponseEntity<List<SearchStoreDto>> searchStore(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable String filter) {
        List<SearchStoreDto> searchStoreDtos = storeService.getSearchStores(customUserDetails.getId(), filter);
        return ResponseEntity.status(HttpStatus.OK).body(searchStoreDtos);
    }
}
