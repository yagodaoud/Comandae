package com.yagodaoud.comandae.repository.menu;

import com.yagodaoud.comandae.model.menu.MenuItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE MenuItem m SET m.deletedAt = CURRENT_TIMESTAMP WHERE m.category.id = :categoryId")
    void softDeleteByCategoryId(@Param("categoryId") Long categoryId);
}
