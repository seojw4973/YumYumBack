package org.baratie.yumyum.domain.menu.repository;

import org.baratie.yumyum.domain.category.domain.Category;
import org.baratie.yumyum.domain.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
