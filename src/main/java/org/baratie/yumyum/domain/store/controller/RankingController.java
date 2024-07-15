package org.baratie.yumyum.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.store.dto.MainStoreDto;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/top10")
public class RankingController {

    private final StoreService storeService;

    /**
     * 평점 기준 top10 맛집
     */
    @GetMapping
    public ResponseEntity<List<MainStoreDto>> findTop10(@RequestParam String local) {
        List<MainStoreDto> Top10List = storeService.getTop10(local);

        return ResponseEntity.status(HttpStatus.OK).body(Top10List);
    }

    /**
     * 이달의 맛집
     */
    @GetMapping("/month")
    public ResponseEntity<List<MainStoreDto>> findTop10OnMonth(@RequestParam String local) {
        List<MainStoreDto> top10List = storeService.getTop10OnMonth(local);

        return new ResponseEntity(top10List, HttpStatus.OK);
    }

    /**
     * 즐겨찾기 기준 top10 맛집
     */
    @GetMapping("/favorite")
    public ResponseEntity<List<MainStoreDto>> findTop10OnFavorite(@RequestParam String local) {
        List<MainStoreDto> top10List = storeService.getTop10OnFavorite(local);

        return new ResponseEntity<>(top10List, HttpStatus.OK);
    }

    /**
     * 조회수 기준 top10 맛집
     */
    @GetMapping("/views")
    public ResponseEntity<List<MainStoreDto>> findTop10OnViews(@RequestParam String local) {
        List<MainStoreDto> top10List = storeService.getTop10OnViews(local);

        return new ResponseEntity(top10List, HttpStatus.OK);
    }
}
