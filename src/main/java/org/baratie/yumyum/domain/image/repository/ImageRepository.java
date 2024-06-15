package org.baratie.yumyum.domain.image.repository;

import org.baratie.yumyum.domain.category.domain.Category;
import org.baratie.yumyum.domain.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
