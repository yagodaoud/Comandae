package com.yagodaoud.comandae.repository.menu;

import com.yagodaoud.comandae.model.menu.DailyMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyMenuItemRepository extends JpaRepository<DailyMenuItem, Long> {
}
