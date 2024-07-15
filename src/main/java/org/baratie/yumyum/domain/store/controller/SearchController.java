package org.baratie.yumyum.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.store.dto.SearchStoreDto;
import org.baratie.yumyum.domain.store.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    /**
     * 내 주변 맛집
     */
    @GetMapping
    public ResponseEntity<List<SearchStoreDto>> findNearbyStore(@RequestParam Double lng, @RequestParam Double lat) {
        List<SearchStoreDto> nearByStoreList = searchService.getNearByStore(lng, lat);
        return ResponseEntity.status(HttpStatus.OK).body(nearByStoreList);
    }

    /**
     * 검색한 가게 리스트
     */
    @GetMapping("/{filter}")
    public ResponseEntity<List<SearchStoreDto>> searchStore(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable String filter) {
        List<SearchStoreDto> searchStoreDto = searchService.getSearchStores(customUserDetails.getId(), filter);
        return ResponseEntity.status(HttpStatus.OK).body(searchStoreDto);
    }
}
