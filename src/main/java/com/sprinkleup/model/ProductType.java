package com.sprinkleup.model;

import java.util.function.Supplier;

/**
 * The kinds of product SprinkleUp sells. Each type knows how to create
 * an empty object of its own class, so new products are made without
 * a chain of if/else statements.
 */
public enum ProductType {
    CAKE("Cake", "bi-cake2", Cake::new),
    CUPCAKE("Cupcake", "bi-balloon-heart", Cupcake::new),
    BREAD("Bread", "bi-basket", Bread::new),
    PASTRY("Pastry", "bi-cup-hot", Pastry::new);

    private final String label;
    private final String icon;
    private final Supplier<Product> factory;

    ProductType(String label, String icon, Supplier<Product> factory) {
        this.label = label;
        this.icon = icon;
        this.factory = factory;
    }

    public String getLabel() {
        return label;
    }

    /** Bootstrap Icons class used on the pages. */
    public String getIcon() {
        return icon;
    }

    public Product newProduct() {
        return factory.get();
    }
}
