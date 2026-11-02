package com.sprinkleup.service;

import com.sprinkleup.dto.ProductForm;
import com.sprinkleup.model.Order;
import com.sprinkleup.model.Product;
import com.sprinkleup.model.ProductType;
import com.sprinkleup.repository.OrderRepository;
import com.sprinkleup.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService extends AbstractCrudService<Product> {

    public static final int LOW_STOCK_LIMIT = 5;

    private final ProductRepository products;
    private final OrderRepository orders;

    public ProductService(ProductRepository products, OrderRepository orders) {
        super(products);
        this.products = products;
        this.orders = orders;
    }

    @Override
    protected String entityName() {
        return "Product";
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return products.findAllByOrderByNameAsc();
    }

    /** Products for the admin list, optionally filtered by name and type. */
    @Transactional(readOnly = true)
    public List<Product> search(String query, ProductType type) {
        List<Product> found = query == null || query.isBlank()
                ? products.findAllByOrderByNameAsc()
                : products.findByNameContainingIgnoreCaseOrderByNameAsc(query.trim());
        return type == null ? found : found.stream().filter(p -> p.getType() == type).toList();
    }

    /** Products shown in the shop. */
    @Transactional(readOnly = true)
    public List<Product> findForShop(ProductType type) {
        return products.findByAvailableTrueOrderByNameAsc().stream()
                .filter(p -> type == null || p.getType() == type)
                .toList();
    }

    @Transactional(readOnly = true)
    public long countLowStock() {
        return products.countByStockLessThan(LOW_STOCK_LIMIT);
    }

    public Product create(ProductForm form) {
        Product product = form.getType().newProduct(); // the enum picks the right subclass
        form.applyTo(product);
        return save(product);
    }

    public Product update(Long id, ProductForm form) {
        Product product = findById(id);
        if (form.getType() != product.getType()) {
            throw new IllegalArgumentException("The type of an existing product cannot be changed");
        }
        form.applyTo(product);
        return save(product);
    }

    /** Old orders keep the product's name and price, but lose the link to it. */
    @Override
    public void delete(Long id) {
        Product product = findById(id);
        for (Order order : orders.findContaining(product)) {
            order.forgetProduct(product);
        }
        products.delete(product);
    }
}
