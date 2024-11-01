package com.yagodaoud.comandae.repository.menu;

import com.yagodaoud.comandae.model.menu.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
