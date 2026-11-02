package com.sprinkleup.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * What the order forms send: who is ordering, when they collect it,
 * and how many of each product (product id -> quantity).
 * Used by both the public shop and the admin "new order" page.
 */
public class OrderForm {

    /** Set when an admin picks an existing customer. */
    private Long customerId;

    /** Set when a shopper types their own details. */
    private String fullName;
    private String email;
    private String phone;
    private String address;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate pickupDate;

    private String notes;

    private Map<Long, Integer> quantities = new HashMap<>();

    /** Only the products the customer actually asked for. */
    public Map<Long, Integer> getChosenQuantities() {
        Map<Long, Integer> chosen = new HashMap<>();
        quantities.forEach((productId, qty) -> {
            if (qty != null && qty > 0) {
                chosen.put(productId, qty);
            }
        });
        return chosen;
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public LocalDate getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Map<Long, Integer> getQuantities() { return quantities; }
    public void setQuantities(Map<Long, Integer> quantities) { this.quantities = quantities; }
}
