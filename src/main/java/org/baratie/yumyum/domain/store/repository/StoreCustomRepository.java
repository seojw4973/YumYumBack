package org.baratie.yumyum.domain.store.repository;

import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface StoreCustomRepository {

    Optional<Store> existAndDeletedStore(Long storeId);

    StoreDetailDto findStoreDetail(Long memberId, Long storeId);

    Slice<MyFavoriteStoreDto> findFavoriteStore(Long memberId, Map<Long, List<String>> hashtagMap, Map<Long, String> imageMap, Pageable pageable);

    Page<AdminStoreDto> getSimpleStore(Pageable pageable);
}
