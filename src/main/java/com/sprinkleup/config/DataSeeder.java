package com.sprinkleup.config;

import com.sprinkleup.model.*;
import com.sprinkleup.repository.CustomerRepository;
import com.sprinkleup.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/** Fills an empty database with a starter menu and a demo customer. */
@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository products;
    private final CustomerRepository customers;

    public DataSeeder(ProductRepository products, CustomerRepository customers) {
        this.products = products;
        this.customers = customers;
    }

    @Override
    public void run(String... args) {
        if (products.count() == 0) {
            List<Product> menu = List.of(
                    new Cake("Chocolate Fudge Cake", "Rich dark chocolate sponge layered with fudge ganache.",
                            new BigDecimal("4200"), 6, 1.0, "Chocolate", false),
                    new Cake("Strawberry Dream Cake", "Vanilla sponge, fresh strawberries and whipped cream.",
                            new BigDecimal("4500"), 4, 1.5, "Strawberry", true),
                    new Cake("Ribbon Birthday Cake", "The classic Sri Lankan ribbon cake with butter icing.",
                            new BigDecimal("3200"), 8, 1.0, "Vanilla", false),
                    new Cupcake("Rainbow Sprinkle Cupcakes", "Our signature cupcakes covered in sprinkles.",
                            new BigDecimal("220"), 15, 6, "Buttercream"),
                    new Cupcake("Red Velvet Cupcakes", "Soft red velvet with cream cheese frosting.",
                            new BigDecimal("260"), 10, 4, "Cream cheese"),
                    new Bread("Sourdough Loaf", "Slow-fermented for 24 hours with a crackly crust.",
                            new BigDecimal("650"), 12, 600, false),
                    new Bread("Milk Bread", "Soft, fluffy and slightly sweet.",
                            new BigDecimal("380"), 3, 400, true),
                    new Pastry("Fish Bun", "Golden bun stuffed with spicy fish and potato.",
                            new BigDecimal("150"), 30, "Spicy fish", true),
                    new Pastry("Butter Croissant", "Flaky, all-butter French croissant.",
                            new BigDecimal("320"), 20, null, true),
                    new Pastry("Chocolate Éclair", "Choux pastry filled with custard and dipped in chocolate.",
                            new BigDecimal("280"), 14, "Vanilla custard", false));
            products.saveAll(menu);
        }
        if (customers.count() == 0) {
            customers.save(new Customer("Nethmi Perera", "nethmi@example.com", "0771234567", "12 Galle Road, Colombo 03"));
        }
    }
}
