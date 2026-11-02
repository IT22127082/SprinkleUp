package com.sprinkleup.model;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Anything a customer orders (ABSTRACTION). A menu Order and a CustomOrder
 * both extend this (INHERITANCE), and each one answers these questions its
 * own way (POLYMORPHISM), so the account page can list both kinds together.
 */
@MappedSuperclass
public abstract class AbstractOrder extends BaseEntity {

    @ManyToOne(optional = false)
    private Customer customer;

    protected AbstractOrder() {
    }

    protected AbstractOrder(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("An order needs a customer");
        }
        this.customer = customer;
    }

    /** "Menu order" or "Custom cake". */
    public abstract String getKindLabel();

    /** One line describing what was ordered. */
    public abstract String getSummary();

    /** The price, or null when it is not known yet (a custom cake before its quote). */
    public abstract BigDecimal getTotal();

    public abstract String getStatusLabel();

    /** Bootstrap colour for the status badge. */
    public abstract String getStatusBadge();

    /** Pickup date or the date the cake is needed. */
    public abstract LocalDate getDueDate();

    /** Where the customer can see this order on the site. */
    public abstract String getCustomerPath();

    public Customer getCustomer() {
        return customer;
    }

    public boolean belongsTo(User user) {
        return user != null && customer.getId().equals(user.getId());
    }
}
