package com.sprinkleup.dto;

import com.sprinkleup.model.Product;
import com.sprinkleup.model.ProductType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * What the admin product form sends. It holds the shared fields plus the
 * fields of every product type; each Product subclass reads only its own.
 */
public class ProductForm {

    private Long id;

    @NotNull(message = "Choose a product type")
    private ProductType type;

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.00", message = "Price cannot be negative")
    private BigDecimal basePrice;

    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;

    private boolean available = true;

    // Cake
    @Positive(message = "Weight must be more than 0")
    private Double weightKg;
    private String flavour;
    private boolean eggless;

    // Cupcake
    @Min(value = 1, message = "At least 1 per box")
    private Integer piecesPerBox;
    private String frosting;

    // Bread
    @Positive(message = "Weight must be more than 0")
    private Integer weightGrams;
    private boolean sliced;

    // Pastry
    private String filling;
    private boolean servedWarm;

    /** Builds a form pre-filled from an existing product (for the edit page). */
    public static ProductForm from(Product product) {
        ProductForm form = new ProductForm();
        form.id = product.getId();
        form.type = product.getType();
        form.name = product.getName();
        form.description = product.getDescription();
        form.basePrice = product.getBasePrice();
        form.stock = product.getStock();
        form.available = product.isAvailable();
        product.fillSpecs(form); // polymorphic: each subclass adds its own fields
        return form;
    }

    /** Copies the form into a product of the right subclass. */
    public void applyTo(Product product) {
        product.setName(name);
        product.setDescription(description);
        product.setBasePrice(basePrice);
        product.setStock(stock);
        product.setAvailable(available);
        product.applySpecs(this); // polymorphic
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProductType getType() { return type; }
    public void setType(ProductType type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }
    public String getFlavour() { return flavour; }
    public void setFlavour(String flavour) { this.flavour = flavour; }
    public boolean isEggless() { return eggless; }
    public void setEggless(boolean eggless) { this.eggless = eggless; }
    public Integer getPiecesPerBox() { return piecesPerBox; }
    public void setPiecesPerBox(Integer piecesPerBox) { this.piecesPerBox = piecesPerBox; }
    public String getFrosting() { return frosting; }
    public void setFrosting(String frosting) { this.frosting = frosting; }
    public Integer getWeightGrams() { return weightGrams; }
    public void setWeightGrams(Integer weightGrams) { this.weightGrams = weightGrams; }
    public boolean isSliced() { return sliced; }
    public void setSliced(boolean sliced) { this.sliced = sliced; }
    public String getFilling() { return filling; }
    public void setFilling(String filling) { this.filling = filling; }
    public boolean isServedWarm() { return servedWarm; }
    public void setServedWarm(boolean servedWarm) { this.servedWarm = servedWarm; }
}
