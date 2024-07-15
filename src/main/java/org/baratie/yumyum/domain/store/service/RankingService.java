package org.baratie.yumyum.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.store.dto.MainStoreDto;
import org.baratie.yumyum.domain.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RankingService {

    private final StoreRepository storeRepository;

    /**
     * 즐겨찾기 기준 top 10
     * @param local 지역
     * @return 지역에 따른 즐겨찾기 기준 top10
     */
    public List<MainStoreDto> getTop10OnFavorite(String local) {
        return storeRepository.findTop10OnFavorite(local);
    }

    /**
     * 조회수 기준 top 10
     * @param local 지역
     * @return 조회수에 따른 즐겨찾기 기준 top10
     */
    public List<MainStoreDto> getTop10OnViews(String local) {
        return storeRepository.findTop10OnViews(local);
    }

    /**
     * 이달의 맛집
     * @param local 지역
     * @return 리뷰가 10개이상인 가게와 가게 전체 평균 리뷰점수보다 높은 가게
     */
    public List<MainStoreDto> getTop10OnMonth(String local) {

        LocalDateTime now = LocalDateTime.now();

        int year = now.getYear();
        int month = now.getMonthValue();

        return storeRepository.findTop10OnMonth(local, year, month);
    }

    /**
     * 지역에 따른 Top10 가게 조회
     * @param local
     * @return 해당 지역에 조건에 맞는 Top10 가게 리턴
     */
    public List<MainStoreDto> getTop10(String local) {
        return storeRepository.findTop10(local);
    }


}
