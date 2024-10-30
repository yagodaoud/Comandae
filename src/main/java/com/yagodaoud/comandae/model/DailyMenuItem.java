package com.yagodaoud.comandae.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "menu_header")
@SQLDelete(sql = "UPDATE menu_header SET deleted_at = NOW() WHERE id=?")
@FilterDef(name = "deletedDailyMenuItemFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedDailyMenuItemFilter", condition = "deleted_at IS NULL")
public class DailyMenuItem {

    @EmbeddedId
    private DailyMenuItemId id = new DailyMenuItemId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("menuId")
    @JoinColumn(name = "menu_id", nullable = false)
    private DailyMenu dailyMenu;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("menuItemId")
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public DailyMenuItem() {}

    public DailyMenuItemId getId() {
        return id;
    }

    public void setId(DailyMenuItemId id) {
        this.id = id;
    }

    public DailyMenu getDailyMenu() {
        return dailyMenu;
    }

    public void setDailyMenu(DailyMenu dailyMenu) {
        this.dailyMenu = dailyMenu;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Embeddable
    public static class DailyMenuItemId implements Serializable {

        @Column(name = "menu_id")
        private Long menuId;

        @Column(name = "menu_item_id")
        private Long menuItemId;

        public Long getMenuId() {
            return menuId;
        }

        public void setMenuId(Long menuId) {
            this.menuId = menuId;
        }

        public Long getMenuItemId() {
            return menuItemId;
        }

        public void setMenuItemId(Long menuItemId) {
            this.menuItemId = menuItemId;
        }
    }
}
