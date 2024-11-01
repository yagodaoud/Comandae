package com.yagodaoud.comandae.dto.menu;

public class DailyMenuItemDTO {
    private MenuItemDTO menuItemDTO;
    private DailyMenuDTO dailyMenuDTO;

    public DailyMenuDTO getDailyMenuDTO() {
        return dailyMenuDTO;
    }

    public void setDailyMenuDTO(DailyMenuDTO dailyMenuDTO) {
        this.dailyMenuDTO = dailyMenuDTO;
    }

    public MenuItemDTO getMenuItemDTO() {
        return menuItemDTO;
    }

    public void setMenuItemDTO(MenuItemDTO menuItemDTO) {
        this.menuItemDTO = menuItemDTO;
    }
}