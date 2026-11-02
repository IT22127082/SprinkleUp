package com.sprinkleup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

/**
 * A person who orders from SprinkleUp. They may have an online account,
 * or be a guest who ordered without signing up.
 */
@Entity
@Table(name = "customers")
public class Customer extends User {

    @Size(max = 255)
    private String address;

    public Customer() {
    }

    public Customer(String fullName, String email, String phone, String address) {
        super(fullName, email, phone);
        this.address = address;
    }

    @Override
    public UserRole getRole() {
        return UserRole.CUSTOMER;
    }

    @Override
    public String getHomePath() {
        return "/account";
    }

    /** Copies the editable details from another customer object (used by the edit form). */
    public void updateDetails(Customer source) {
        setFullName(source.getFullName());
        setEmail(source.getEmail());
        setPhone(source.getPhone());
        setAddress(source.getAddress());
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null || address.isBlank() ? null : address.trim();
    }
}
