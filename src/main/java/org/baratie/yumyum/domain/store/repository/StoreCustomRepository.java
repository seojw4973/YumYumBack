package org.baratie.yumyum.domain.store.repository;

import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.AdminStoreDto;
import org.baratie.yumyum.domain.store.dto.MainStoreDto;
import org.baratie.yumyum.domain.store.dto.MyFavoriteStoreDto;
import org.baratie.yumyum.domain.store.dto.StoreDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreCustomRepository {

    Optional<Store> existAndDeletedStore(Long storeId);

    List<MainStoreDto> findTop10(String local);

    List<MainStoreDto> findTop10OnViews(String local);

    List<MainStoreDto> findTop10OnFavorite(String local);

    StoreDetailDto findStoreDetail(Long memberId, Long storeId);

    Slice<MyFavoriteStoreDto> findFavoriteStore(Long memberId, Pageable pageable);

    List<MainStoreDto> findTop10OnMonth(String local, int year, int month);

    List<SearchStoreDto> findSearchStore(Long memberId, String keyword);

    List<SearchStoreDto> findNearByStore(Double lng, Double lat);
}
