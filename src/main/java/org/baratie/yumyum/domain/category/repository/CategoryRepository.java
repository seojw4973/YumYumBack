package org.baratie.yumyum.domain.category.repository;

import org.baratie.yumyum.domain.category.domain.Category;
import org.baratie.yumyum.domain.category.dto.CategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c.name FROM Category c Where c.store.id = :storeId")
    List<String> findByStoreId(Long storeId);
}
