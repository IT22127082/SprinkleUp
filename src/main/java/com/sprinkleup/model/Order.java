package com.sprinkleup.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An order for items from the menu. The item list is private and only changed
 * through addItem(), which also takes the items out of stock (INFORMATION HIDING).
 */
@Entity
@Table(name = "orders")
public class Order extends AbstractOrder {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    private LocalDate pickupDate;

    @Column(length = 500)
    private String notes;

    protected Order() {
    }

    public Order(Customer customer, LocalDate pickupDate, String notes) {
        super(customer);
        this.pickupDate = pickupDate;
        setNotes(notes);
    }

    public void addItem(Product product, int quantity) {
        product.reduceStock(quantity);
        items.add(new OrderItem(this, product, quantity));
    }

    @Override
    public BigDecimal getTotal() {
        return items.stream().map(OrderItem::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getItemCount() {
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    /** Moves the order to a new stage. Cancelling puts the items back in stock. */
    public void changeStatus(OrderStatus newStatus) {
        if (status.isClosed() && newStatus != status) {
            throw new IllegalStateException("A " + status.getLabel().toLowerCase() + " order cannot be changed");
        }
        if (newStatus == OrderStatus.CANCELLED) {
            returnItemsToStock();
        }
        this.status = newStatus;
    }

    /** Puts the items back on the shelf, unless the order was already delivered or cancelled. */
    public void returnItemsToStock() {
        if (status.isClosed()) {
            return;
        }
        for (OrderItem item : items) {
            if (item.getProduct() != null) {
                item.getProduct().restock(item.getQuantity());
            }
        }
    }

    /** Called before a product is deleted so this order keeps its history. */
    public void forgetProduct(Product product) {
        items.stream().filter(i -> i.getProduct() != null && i.getProduct().getId().equals(product.getId()))
                .forEach(OrderItem::detachProduct);
    }

    @Override
    public String getDisplayName() {
        return "Order #" + getId();
    }

    @Override
    public String getKindLabel() {
        return "Menu order";
    }

    @Override
    public String getSummary() {
        int count = getItemCount();
        String first = items.isEmpty() ? "" : items.get(0).getProductName().split(" — ")[0];
        return count + (count == 1 ? " item" : " items") + (first.isEmpty() ? "" : " · " + first + (items.size() > 1 ? " and more" : ""));
    }

    @Override
    public String getStatusLabel() {
        return status.getLabel();
    }

    @Override
    public String getStatusBadge() {
        return status.getBadge();
    }

    @Override
    public LocalDate getDueDate() {
        return pickupDate;
    }

    @Override
    public String getCustomerPath() {
        return "/account/orders/" + getId();
    }

    /** Read-only view: callers cannot add or remove items directly. */
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes == null || notes.isBlank() ? null : notes.trim();
    }
}
