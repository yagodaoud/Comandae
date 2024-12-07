    package com.yagodaoud.comandae.model;

    import jakarta.persistence.*;
    import org.hibernate.annotations.Filter;
    import org.hibernate.annotations.FilterDef;
    import org.hibernate.annotations.ParamDef;
    import org.hibernate.annotations.SQLDelete;

    import java.time.LocalDateTime;

    @Entity
    @Table(name = "pix")
    @SQLDelete(sql = "UPDATE pix SET deleted_at = NOW() WHERE id=?")
    @FilterDef(name = "deletedPixFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
    @Filter(name = "deletedPixFilter", condition = "deleted_at IS NULL")
    public class Pix {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Enumerated(EnumType.STRING)
        @Column(name = "type", nullable = false)
        private PixType type;

        @Column(name = "pix_key", nullable = false)
        private String key;

        @Column(name = "is_active", nullable = false)
        private Boolean isActive;

        @Column(name = "created_at", nullable = false, updatable = false)
        private final LocalDateTime createdAt;

        @Column(name = "deleted_at")
        private LocalDateTime deletedAt;

        public Pix() {
            this.createdAt = LocalDateTime.now();
            this.isActive = true;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public PixType getType() {
            return type;
        }

        public void setType(PixType type) {
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
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
    }

