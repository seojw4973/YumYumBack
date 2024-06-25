package org.baratie.yumyum.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.store.dto.StoreDetailDto;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/store/{store_id}")
    public ResponseEntity<StoreDetailDto> findStore(@PathVariable("store_id") Long store_id) {
        StoreDetailDto storeDetailDto = storeService.StoreDetail(store_id);
        return ResponseEntity.ok(storeDetailDto);
    }

}
