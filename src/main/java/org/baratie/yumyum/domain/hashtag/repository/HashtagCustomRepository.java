package org.baratie.yumyum.domain.hashtag.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface HashtagCustomRepository {

    Map<Long, List<String>> findHashtagByStoreId();
}
