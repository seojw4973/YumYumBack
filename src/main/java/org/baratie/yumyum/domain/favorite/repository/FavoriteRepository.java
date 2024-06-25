package org.baratie.yumyum.domain.favorite.repository;

import org.baratie.yumyum.domain.category.domain.Category;
import org.baratie.yumyum.domain.favorite.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteCustomRepository {

    int countFavoriteByStoreId(Long storeId);
}
