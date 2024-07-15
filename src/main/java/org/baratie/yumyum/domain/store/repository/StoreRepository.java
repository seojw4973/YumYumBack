package org.baratie.yumyum.domain.store.repository;

import org.baratie.yumyum.domain.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, StoreCustomRepository, SearchCustomRepository, RankingCustomRepository{

    boolean existsByName(String storeName);
}
