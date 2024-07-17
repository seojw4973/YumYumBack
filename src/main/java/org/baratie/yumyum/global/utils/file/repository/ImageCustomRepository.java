package org.baratie.yumyum.global.utils.file.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ImageCustomRepository {

    Map<Long, List<String>> findImageByReviewIdList();

    Map<Long, List<String>> findImageByStoreIdList();
}
