package com.sprinkleup.repository;

import com.sprinkleup.model.CustomOrder;
import com.sprinkleup.model.CustomOrderStatus;
import com.sprinkleup.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CustomOrderRepository extends JpaRepository<CustomOrder, Long> {

    List<CustomOrder> findAllByOrderByCreatedAtDesc();

    List<CustomOrder> findByStatusOrderByCreatedAtDesc(CustomOrderStatus status);

    List<CustomOrder> findByCustomerOrderByCreatedAtDesc(Customer customer);

    List<CustomOrder> findByStatusIn(Collection<CustomOrderStatus> statuses);

    long countByStatus(CustomOrderStatus status);

    boolean existsByCustomer(Customer customer);
}
