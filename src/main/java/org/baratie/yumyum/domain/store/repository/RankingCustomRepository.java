package org.baratie.yumyum.domain.store.repository;

import org.baratie.yumyum.domain.store.dto.MainStoreDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankingCustomRepository {

    List<MainStoreDto> findTop10(String local);

    List<MainStoreDto> findTop10OnViews(String local);

    List<MainStoreDto> findTop10OnFavorite(String local);

    List<MainStoreDto> findTop10OnMonth(String local, int year, int month);

}
