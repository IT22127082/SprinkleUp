package com.sprinkleup.model;

import com.sprinkleup.dto.ProductForm;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/** A single pastry, sweet or savoury, sold at its base price. */
@Entity
@DiscriminatorValue("PASTRY")
public class Pastry extends Product {

    private String filling;
    private Boolean servedWarm = false;

    public Pastry() {
    }

    public Pastry(String name, String description, BigDecimal price, int stock, String filling, boolean servedWarm) {
        super(name, description, price, stock);
        setFilling(filling);
        setServedWarm(servedWarm);
    }

    @Override
    public ProductType getType() {
        return ProductType.PASTRY;
    }

    @Override
    public BigDecimal getUnitPrice() {
        return getBasePrice();
    }

    @Override
    public String getSpecs() {
        String specs = filling == null ? "Plain" : filling + " filling";
        return isServedWarm() ? specs + " · Best served warm" : specs;
    }

    @Override
    public void applySpecs(ProductForm form) {
        setFilling(form.getFilling());
        setServedWarm(form.isServedWarm());
    }

    @Override
    public void fillSpecs(ProductForm form) {
        form.setFilling(filling);
        form.setServedWarm(isServedWarm());
    }

    public String getFilling() {
        return filling;
    }

    public void setFilling(String filling) {
        this.filling = filling == null || filling.isBlank() ? null : filling.trim();
    }

    public boolean isServedWarm() {
        return Boolean.TRUE.equals(servedWarm);
    }

    public void setServedWarm(boolean servedWarm) {
        this.servedWarm = servedWarm;
    }
}
