package org.baratie.yumyum.domain.store.controller;

import com.google.maps.errors.ApiException;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.store.dto.CreateStoreDto;
import org.baratie.yumyum.domain.store.dto.MainStoreDto;
import org.baratie.yumyum.domain.store.dto.StoreDetailDto;
import org.baratie.yumyum.domain.store.dto.UpdateStoreDto;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<StoreDetailDto> findStore(@PathVariable("storeId") Long store_id) {
        StoreDetailDto storeDetailDto = storeService.StoreDetail(store_id);
        return ResponseEntity.ok(storeDetailDto);
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

}
