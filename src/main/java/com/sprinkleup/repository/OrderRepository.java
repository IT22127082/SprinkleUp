package com.sprinkleup.repository;

import com.sprinkleup.model.Customer;
import com.sprinkleup.model.Order;
import com.sprinkleup.model.OrderStatus;
import com.sprinkleup.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByCreatedAtDesc();

    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);

    List<Order> findTop5ByOrderByCreatedAtDesc();

    long countByStatus(OrderStatus status);

    boolean existsByCustomer(Customer customer);

    @Query("select distinct i.order from OrderItem i where i.product = :product")
    List<Order> findContaining(Product product);
}
