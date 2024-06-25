package org.baratie.yumyum.domain.favorite.repository;

import org.baratie.yumyum.domain.favorite.domain.Favorite;

import java.util.Optional;

public interface FavoriteCustomRepository {

    Optional<Favorite> exist(Long memberId, Long storeId);
}
