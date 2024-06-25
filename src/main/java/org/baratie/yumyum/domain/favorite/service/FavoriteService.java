package org.baratie.yumyum.domain.favorite.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.favorite.domain.Favorite;
import org.baratie.yumyum.domain.favorite.dto.FavoriteDto;
import org.baratie.yumyum.domain.favorite.repository.FavoriteRepository;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.exception.StoreNotFoundException;
import org.baratie.yumyum.domain.store.repository.StoreRepository;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final StoreRepository storeRepository;
    private final FavoriteRepository favoriteRepository;

    public void addFavorite(Member member, FavoriteDto request) {

        Store store = validationStoreId(request.storeId());

        favoriteRepository.exist(member.getId(), store.getId()).ifPresentOrElse(
                favorite -> {
                    favorite.changeFavoriteStatus(request.isFavorite());
                    favoriteRepository.save(favorite);
                },
                () -> favoriteRepository.save(Favorite.insertFavorite(member, store))
        );

    }

    /**
     * 가게가 DB에 존재하는지 확인
     * @param storeId 가게 pk
     * @return Store
     */
    private Store validationStoreId(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(
                () -> new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND)
        );
    }
}
