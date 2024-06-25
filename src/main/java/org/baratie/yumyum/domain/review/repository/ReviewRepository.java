package org.baratie.yumyum.domain.review.repository;

import org.baratie.yumyum.domain.category.domain.Category;
import org.baratie.yumyum.domain.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    int countReviewByStoreId(Long storeId);
}
