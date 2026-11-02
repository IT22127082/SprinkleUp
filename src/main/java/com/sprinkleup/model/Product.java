package com.sprinkleup.model;

import com.sprinkleup.dto.ProductForm;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A bakery item for sale (ABSTRACTION).
 * Cake, Cupcake, Bread and Pastry extend this class (INHERITANCE) and each one
 * decides how its own price and details are worked out (POLYMORPHISM).
 * All fields are private and changed only through checked setters (ENCAPSULATION).
 */
@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type")
public abstract class Product extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice = BigDecimal.ZERO;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private boolean available = true;

    protected Product() {
    }

    protected Product(String name, String description, BigDecimal basePrice, int stock) {
        setName(name);
        setDescription(description);
        setBasePrice(basePrice);
        setStock(stock);
    }

    // ---------- Polymorphic behaviour each subclass must provide ----------

    /** The category shown to customers, e.g. "Cake". */
    public abstract ProductType getType();

    /** Price for one unit as sold (one cake, one box of cupcakes...). */
    public abstract BigDecimal getUnitPrice();

    /** A short line describing the subclass-specific details. */
    public abstract String getSpecs();

    /** Copies this type's own fields (weight, flavour...) from the admin form. */
    public abstract void applySpecs(ProductForm form);

    /** Writes this type's own fields into the admin form for editing. */
    public abstract void fillSpecs(ProductForm form);

    // ---------- Shared behaviour ----------

    public BigDecimal priceFor(int quantity) {
        return getUnitPrice().multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isInStock() {
        return available && stock > 0;
    }

    public boolean canSupply(int quantity) {
        return available && quantity > 0 && quantity <= stock;
    }

    /** Takes items out of stock when an order is placed. */
    public void reduceStock(int quantity) {
        if (!canSupply(quantity)) {
            throw new IllegalStateException("Only " + stock + " of " + name + " left in stock");
        }
        stock -= quantity;
    }

    /** Puts items back when an order is cancelled or deleted. */
    public void restock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        stock += quantity;
    }

    @Override
    public String getDisplayName() {
        return name + " (" + getType().getLabel() + ")";
    }

    // ---------- Getters / validated setters ----------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        this.name = name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        if (basePrice == null || basePrice.signum() < 0) {
            throw new IllegalArgumentException("Price must be zero or more");
        }
        this.basePrice = basePrice.setScale(2, RoundingMode.HALF_UP);
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stock = stock;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
