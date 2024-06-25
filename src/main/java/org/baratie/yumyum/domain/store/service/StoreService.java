package org.baratie.yumyum.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.favorite.repository.FavoriteRepository;
import org.baratie.yumyum.domain.review.repository.ReviewRepository;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.dto.StoreDetailDto;
import org.baratie.yumyum.domain.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;

    /**
     * 맛집 상세 조회
     * @param store_id 가게 pk
     * @return StoreDetailDto
     */
    public StoreDetailDto StoreDetail(Long store_id){
        Store store = validationStoreId(store_id);

        int reviewCount = reviewRepository.countReviewByStoreId(store_id);
        int favoriteCount = favoriteRepository.countFavoriteByStoreId(store_id);

        return StoreDetailDto.fromEntity(store, reviewCount, favoriteCount);
    }


    /**
     *
     * @param store_id 가게 pk
     * @return Store
     */
    private Store validationStoreId(Long store_id) {
        return storeRepository.findById(store_id).orElseThrow(
                () -> new NotFoundException("존재하지 않는 가게입니다.")
        );
    }

}
