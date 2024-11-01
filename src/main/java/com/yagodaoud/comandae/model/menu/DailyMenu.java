package com.yagodaoud.comandae.model.menu;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menu_header")
@SQLDelete(sql = "UPDATE menu_header SET deleted_at = NOW() WHERE id=?")
@FilterDef(name = "deletedDailyMenuFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedDailyMenuFilter", condition = "deleted_at IS NULL")
public class DailyMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "header_id", referencedColumnName = "id")
    private MenuHeader header;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "footer_id", referencedColumnName = "id")
    private MenuFooter footer;

    @Column(name = "created_at", nullable = false, updatable = false)
    private final LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "dailyMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DailyMenuItem> items = new HashSet<>();

    public DailyMenu() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MenuHeader getHeader() {
        return header;
    }

    public void setHeader(MenuHeader header) {
        this.header = header;
    }

    public MenuFooter getFooter() {
        return footer;
    }

    public void setFooter(MenuFooter footer) {
        this.footer = footer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Set<DailyMenuItem> getItems() {
        return items;
    }

    public void setItems(Set<DailyMenuItem> items) {
        this.items = items;
    }
}
