package com.sprinkleup.repository;

import com.sprinkleup.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByNameAsc();

    List<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    List<Product> findByAvailableTrueOrderByNameAsc();

    long countByStockLessThan(int stock);
}
