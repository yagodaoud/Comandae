package com.yagodaoud.comandae.dto.menu;

import java.util.List;

public class DailyMenuDTO {
    private Long id;
    private Long headerId;
    private Long footerId;
    private List<Long> itemIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public Long getFooterId() {
        return footerId;
    }

    public void setFooterId(Long footerId) {
        this.footerId = footerId;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
}
