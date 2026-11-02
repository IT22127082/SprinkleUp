package com.sprinkleup.model;

/** The stages of a custom cake request. */
public enum CustomOrderStatus {
    REQUESTED("Awaiting quote", "warning"),
    QUOTED("Quote ready", "info"),
    ACCEPTED("Accepted", "primary"),
    IN_PROGRESS("Being made", "primary"),
    COMPLETED("Completed", "success"),
    DECLINED("Declined", "secondary"),
    CANCELLED("Cancelled", "secondary");

    private final String label;
    private final String badge;

    CustomOrderStatus(String label, String badge) {
        this.label = label;
        this.badge = badge;
    }

    public String getLabel() {
        return label;
    }

    public String getBadge() {
        return badge;
    }

    /** Finished requests can no longer change. */
    public boolean isClosed() {
        return this == COMPLETED || this == DECLINED || this == CANCELLED;
    }

    /** The customer can back out until the bakery starts making the cake. */
    public boolean isCancellableByCustomer() {
        return this == REQUESTED || this == QUOTED || this == ACCEPTED;
    }

    /** These stages need an agreed price. */
    public boolean needsPrice() {
        return this == ACCEPTED || this == IN_PROGRESS || this == COMPLETED;
    }
}
