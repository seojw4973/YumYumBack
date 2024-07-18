package org.baratie.yumyum.domain.hashtag.repository;

import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long>, HashtagCustomRepository {

    @Query("SELECT h.content FROM Hashtag h where h.store.id = :storeId")
    List<String> findByStoreId(Long storeId);
}
