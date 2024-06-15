package org.baratie.yumyum.domain.likes.repository;

import org.baratie.yumyum.domain.category.domain.Category;
import org.baratie.yumyum.domain.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
}
