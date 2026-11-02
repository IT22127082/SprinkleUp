package com.sprinkleup.model;

/** The stages an order moves through. */
public enum OrderStatus {
    PENDING("Pending", "warning"),
    BAKING("Baking", "info"),
    READY("Ready for pickup", "primary"),
    DELIVERED("Delivered", "success"),
    CANCELLED("Cancelled", "secondary");

    private final String label;
    private final String badge;

    OrderStatus(String label, String badge) {
        this.label = label;
        this.badge = badge;
    }

    public String getLabel() {
        return label;
    }

    /** Bootstrap colour used for the status badge. */
    public String getBadge() {
        return badge;
    }

    /** Delivered and cancelled orders can no longer change. */
    public boolean isClosed() {
        return this == DELIVERED || this == CANCELLED;
    }
}
