package com.vanrin05.app.repository;

import com.vanrin05.app.domain.HOME_CATEGORY_SECTION;
import com.vanrin05.app.model.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long> {
    List<HomeCategory> findByHomeCategorySection(HOME_CATEGORY_SECTION section);
}
