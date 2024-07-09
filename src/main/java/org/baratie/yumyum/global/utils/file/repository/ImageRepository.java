package org.baratie.yumyum.global.utils.file.repository;

import org.baratie.yumyum.global.utils.file.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    /**
     * @param reviewId 갖고올 이미지의 review id
     * @return 이미지 Url
     */
    @Query("SELECT i.imageUrl FROM Image i WHERE i.review.id = :reviewId")
    List<String> findByReviewId(Long reviewId);

    @Query("SELECT i.imageUrl FROM Image i WHERE i.store.id = :storeId")
    List<String> findByStoreId(Long storeId);
}
