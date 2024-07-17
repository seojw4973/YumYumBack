package org.baratie.yumyum.domain.hashtag.repository;

import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long>, HashtagCustomRepository {

    List<Hashtag> findByStoreId(Long storeId);
}
