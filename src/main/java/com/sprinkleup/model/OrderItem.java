package com.sprinkleup.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * One line of an order. The product name and price are copied at the time
 * of ordering, so later price changes do not alter old orders.
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, length = 140)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    protected OrderItem() {
    }

    /** Only Order creates items, so this constructor is package-private. */
    OrderItem(Order order, Product product, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        this.order = order;
        this.product = product;
        this.productName = product.getName() + " — " + product.getSpecs();
        this.quantity = quantity;
        this.unitPrice = product.getUnitPrice();
    }

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /** Clears the link to a product that is being deleted; the copied name and price stay. */
    void detachProduct() {
        this.product = null;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}
