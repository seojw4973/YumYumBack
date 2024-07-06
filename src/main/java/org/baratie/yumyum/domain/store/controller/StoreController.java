package org.baratie.yumyum.domain.store.controller;

import com.google.maps.errors.ApiException;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.store.dto.*;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    /**
     * 평점 asc 즐겨찾기 asc top10 맛집
     */
    @GetMapping("/top10")
    public ResponseEntity<List<MainStoreDto>> findTop10(@RequestParam String local) {
        List<MainStoreDto> Top10List = storeService.getTop10(local);

        return ResponseEntity.status(HttpStatus.OK).body(Top10List);
    }

    /**
     * 가게 상세 정보
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDetailDto> findStore(@PathVariable("storeId") Long storeId) {
        StoreDetailDto storeDetailDto = storeService.StoreDetail(storeId);
        return ResponseEntity.ok(storeDetailDto);
    }

    /**
     * 내가 즐겨찾기한 가게
     */
    @GetMapping("/favorite")
    ResponseEntity<Slice<MyFavoriteStoreDto>> myFavoriteStore(@AuthenticationPrincipal CustomUserDetails customUserDetails,@RequestParam int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 5);

        Slice<MyFavoriteStoreDto> myFavoriteStore = storeService.getMyFavoriteStore(customUserDetails.getId(), pageable);

        return new ResponseEntity<>(myFavoriteStore, HttpStatus.ACCEPTED);
    }

    /**
     * 가게 등록
     */
    @PostMapping
    public ResponseEntity<Void> registerStore(@RequestBody CreateStoreDto createStoreDto) throws IOException, InterruptedException, ApiException {
        storeService.createStore(createStoreDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 가게 정보 수정
     */
    @PatchMapping("/{storeId}")
    public ResponseEntity<Void> updateStore(@PathVariable Long storeId, @RequestBody UpdateStoreDto request) {
        storeService.updateStore(storeId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 관리자 페이지 맛집 전체 조회
     */
    @GetMapping("/admin")
    public ResponseEntity<Page<AdminStoreDto>> findAllAdmin(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<AdminStoreDto> adminStoreDto = storeService.getAdminStores(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(adminStoreDto);
    }

}
