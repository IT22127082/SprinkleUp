package com.sprinkleup.model;

import com.sprinkleup.dto.ProductForm;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** A cake priced by weight; eggless cakes cost 10% more. */
@Entity
@DiscriminatorValue("CAKE")
public class Cake extends Product {

    private static final BigDecimal EGGLESS_SURCHARGE = new BigDecimal("1.10");

    private Double weightKg = 1.0;
    private String flavour;
    private Boolean eggless = false;

    public Cake() {
    }

    public Cake(String name, String description, BigDecimal pricePerKg, int stock,
                double weightKg, String flavour, boolean eggless) {
        super(name, description, pricePerKg, stock);
        setWeightKg(weightKg);
        setFlavour(flavour);
        setEggless(eggless);
    }

    @Override
    public ProductType getType() {
        return ProductType.CAKE;
    }

    @Override
    public BigDecimal getUnitPrice() {
        BigDecimal price = getBasePrice().multiply(BigDecimal.valueOf(weightKg));
        if (isEggless()) {
            price = price.multiply(EGGLESS_SURCHARGE);
        }
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getSpecs() {
        return weightKg + " kg · " + (flavour == null ? "Classic" : flavour) + (isEggless() ? " · Eggless" : "");
    }

    @Override
    public void applySpecs(ProductForm form) {
        setWeightKg(form.getWeightKg() == null ? 1.0 : form.getWeightKg());
        setFlavour(form.getFlavour());
        setEggless(form.isEggless());
    }

    @Override
    public void fillSpecs(ProductForm form) {
        form.setWeightKg(weightKg);
        form.setFlavour(flavour);
        form.setEggless(isEggless());
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Cake weight must be more than 0 kg");
        }
        this.weightKg = weightKg;
    }

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour == null || flavour.isBlank() ? null : flavour.trim();
    }

    public boolean isEggless() {
        return Boolean.TRUE.equals(eggless);
    }

    public void setEggless(boolean eggless) {
        this.eggless = eggless;
    }
}
