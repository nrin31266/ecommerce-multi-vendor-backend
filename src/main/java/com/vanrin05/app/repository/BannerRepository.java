package com.vanrin05.app.repository;

import com.vanrin05.app.domain.BANNER_TARGET_TYPE;
import com.vanrin05.app.model.Banner;
import org.hibernate.tool.schema.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Integer> {
    @Query("SELECT b FROM Banner b WHERE b.active = true AND :now BETWEEN b.startDate AND b.endDate")
    List<Banner> findActiveBanners(@Param("now") LocalDateTime now);


    List<Banner> findByTargetType(BANNER_TARGET_TYPE targetType);

}
