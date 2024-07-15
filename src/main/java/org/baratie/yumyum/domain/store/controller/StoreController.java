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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    /**
     * 평점 기준 top10 맛집
     */
    @GetMapping("/top10")
    public ResponseEntity<List<MainStoreDto>> findTop10(@RequestParam String local) {
        List<MainStoreDto> Top10List = storeService.getTop10(local);

        return ResponseEntity.status(HttpStatus.OK).body(Top10List);
    }

    /**
     * 이달의 맛집
     */
    @GetMapping("/top10/month")
    public ResponseEntity<List<MainStoreDto>> findTop10OnMonth(@RequestParam String local) {
        List<MainStoreDto> top10List = storeService.getTop10OnMonth(local);

        return new ResponseEntity(top10List, HttpStatus.OK);
    }

    /**
     * 즐겨찾기 기준 top10 맛집
     */
    @GetMapping("/top10/favorite")
    public ResponseEntity<List<MainStoreDto>> findTop10OnFavorite(@RequestParam String local) {
        List<MainStoreDto> top10List = storeService.getTop10OnFavorite(local);

        return new ResponseEntity<>(top10List, HttpStatus.OK);
    }

    /**
     * 조회수 기준 top10 맛집
     */
    @GetMapping("/top10/views")
    public ResponseEntity<List<MainStoreDto>> findTop10OnViews(@RequestParam String local) {
        List<MainStoreDto> top10List = storeService.getTop10OnViews(local);

        return new ResponseEntity(top10List, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SearchStoreDto>> findNearbyStore(@RequestParam Double lng, @RequestParam Double lat) {
        List<SearchStoreDto> nearByStoreList = storeService.getNearByStore(lng, lat);
        return ResponseEntity.status(HttpStatus.OK).body(nearByStoreList);
    }


    /**
     * 가게 상세 정보
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDetailDto> findStore(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("storeId") Long storeId) {
        Long memberId = customUserDetails.getId();
        StoreDetailDto storeDetailDto = storeService.StoreDetail(memberId, storeId);
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
     * 검색한 가게 리스트
     */
    @GetMapping("/search/{filter}")
    public ResponseEntity<List<SearchStoreDto>> searchStore(@AuthenticationPrincipal CustomUserDetails customUserDetails,@PathVariable String filter) {
        List<SearchStoreDto> searchStoreDtos = storeService.getSearchStores(customUserDetails.getId(), filter);
        return ResponseEntity.status(HttpStatus.OK).body(searchStoreDtos);
    }

    /**
     * 가게 등록
     */
    @PostMapping
    public ResponseEntity<Void> registerStore(@RequestPart("createStoreDto") CreateStoreDto createStoreDto,
                                              @RequestPart("files") List<MultipartFile> files) throws IOException, InterruptedException, ApiException {
        storeService.createStore(createStoreDto, files);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 가게 정보 수정
     */
    @PatchMapping("/{storeId}")
    public ResponseEntity<Void> updateStore(@PathVariable Long storeId,
                                            @RequestBody UpdateStoreDto request,
                                            @RequestPart List<MultipartFile> files) {
        storeService.updateStore(storeId, request, files);
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
