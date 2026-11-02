package com.sprinkleup.model;

public enum UserRole {
    CUSTOMER("Customer"),
    ADMIN("Bakery staff");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
