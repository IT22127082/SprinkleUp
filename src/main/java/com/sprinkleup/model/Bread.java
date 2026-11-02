package com.sprinkleup.model;

import com.sprinkleup.dto.ProductForm;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/** A loaf of bread; slicing adds a small fixed charge. */
@Entity
@DiscriminatorValue("BREAD")
public class Bread extends Product {

    private static final BigDecimal SLICING_CHARGE = new BigDecimal("20.00");

    private Integer weightGrams = 400;
    private Boolean sliced = false;

    public Bread() {
    }

    public Bread(String name, String description, BigDecimal price, int stock, int weightGrams, boolean sliced) {
        super(name, description, price, stock);
        setWeightGrams(weightGrams);
        setSliced(sliced);
    }

    @Override
    public ProductType getType() {
        return ProductType.BREAD;
    }

    @Override
    public BigDecimal getUnitPrice() {
        return isSliced() ? getBasePrice().add(SLICING_CHARGE) : getBasePrice();
    }

    @Override
    public String getSpecs() {
        return weightGrams + " g loaf" + (isSliced() ? " · Sliced" : "");
    }

    @Override
    public void applySpecs(ProductForm form) {
        setWeightGrams(form.getWeightGrams() == null ? 400 : form.getWeightGrams());
        setSliced(form.isSliced());
    }

    @Override
    public void fillSpecs(ProductForm form) {
        form.setWeightGrams(weightGrams);
        form.setSliced(isSliced());
    }

    public Integer getWeightGrams() {
        return weightGrams;
    }

    public void setWeightGrams(int weightGrams) {
        if (weightGrams <= 0) {
            throw new IllegalArgumentException("Loaf weight must be more than 0 g");
        }
        this.weightGrams = weightGrams;
    }

    public boolean isSliced() {
        return Boolean.TRUE.equals(sliced);
    }

    public void setSliced(boolean sliced) {
        this.sliced = sliced;
    }
}
