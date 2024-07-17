package org.baratie.yumyum.domain.store.repository;

import org.baratie.yumyum.domain.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, StoreCustomRepository, SearchCustomRepository, RankingCustomRepository{

    boolean existsByName(String storeName);

    @Query("SELECT COUNT(s) FROM Store s WHERE s.id = :storeId AND s.isClosed = false")
    Long existAndNotCloseStore(@Param("storeId") Long storeId);
}
