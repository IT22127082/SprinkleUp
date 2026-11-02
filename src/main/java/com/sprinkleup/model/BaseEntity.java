package com.sprinkleup.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Root of every stored object (ABSTRACTION + INHERITANCE).
 * The id and timestamps are managed here only: there is no setter for them,
 * so no other class can change them (INFORMATION HIDING).
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isNew() {
        return id == null;
    }

    /** Every entity must be able to describe itself in one line (polymorphic). */
    public abstract String getDisplayName();
}
