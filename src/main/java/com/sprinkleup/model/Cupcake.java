package com.sprinkleup.model;

import com.sprinkleup.dto.ProductForm;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Cupcakes sold by the box; the base price is per piece. */
@Entity
@DiscriminatorValue("CUPCAKE")
public class Cupcake extends Product {

    private Integer piecesPerBox = 6;
    private String frosting;

    public Cupcake() {
    }

    public Cupcake(String name, String description, BigDecimal pricePerPiece, int stock,
                   int piecesPerBox, String frosting) {
        super(name, description, pricePerPiece, stock);
        setPiecesPerBox(piecesPerBox);
        setFrosting(frosting);
    }

    @Override
    public ProductType getType() {
        return ProductType.CUPCAKE;
    }

    @Override
    public BigDecimal getUnitPrice() {
        return getBasePrice().multiply(BigDecimal.valueOf(piecesPerBox)).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getSpecs() {
        return "Box of " + piecesPerBox + (frosting == null ? "" : " · " + frosting + " frosting");
    }

    @Override
    public void applySpecs(ProductForm form) {
        setPiecesPerBox(form.getPiecesPerBox() == null ? 6 : form.getPiecesPerBox());
        setFrosting(form.getFrosting());
    }

    @Override
    public void fillSpecs(ProductForm form) {
        form.setPiecesPerBox(piecesPerBox);
        form.setFrosting(frosting);
    }

    public Integer getPiecesPerBox() {
        return piecesPerBox;
    }

    public void setPiecesPerBox(int piecesPerBox) {
        if (piecesPerBox < 1) {
            throw new IllegalArgumentException("A box needs at least 1 cupcake");
        }
        this.piecesPerBox = piecesPerBox;
    }

    public String getFrosting() {
        return frosting;
    }

    public void setFrosting(String frosting) {
        this.frosting = frosting == null || frosting.isBlank() ? null : frosting.trim();
    }
}
