package org.baratie.yumyum.domain.hashtag.repository;

import org.baratie.yumyum.domain.category.domain.Category;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
