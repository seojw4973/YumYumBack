package org.baratie.yumyum.domain.member.controller.admin;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.store.dto.AdminStoreDto;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminStoreController {

    private final StoreService storeService;

    /**
     * 관리자 페이지 맛집 전체 조회
     */
    @GetMapping
    public ResponseEntity<Page<AdminStoreDto>> findAllAdmin(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<AdminStoreDto> adminStoreDto = storeService.getAdminStores(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(adminStoreDto);
    }
}
