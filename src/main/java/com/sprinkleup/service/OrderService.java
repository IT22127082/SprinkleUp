package com.sprinkleup.service;

import com.sprinkleup.dto.OrderForm;
import com.sprinkleup.exception.NotFoundException;
import com.sprinkleup.model.*;
import com.sprinkleup.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderService extends AbstractCrudService<Order> {

    private final OrderRepository orders;
    private final ProductService productService;
    private final CustomerService customerService;

    public OrderService(OrderRepository orders, ProductService productService, CustomerService customerService) {
        super(orders);
        this.orders = orders;
        this.productService = productService;
        this.customerService = customerService;
    }

    @Override
    protected String entityName() {
        return "Order";
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orders.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Order> findByStatus(OrderStatus status) {
        return status == null ? findAll() : orders.findByStatusOrderByCreatedAtDesc(status);
    }

    @Transactional(readOnly = true)
    public List<Order> findRecent() {
        return orders.findTop5ByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Order> findByCustomer(Customer customer) {
        return orders.findByCustomerOrderByCreatedAtDesc(customer);
    }

    /** A customer can only open their own orders; anyone else's look like they don't exist. */
    @Transactional(readOnly = true)
    public Order findOwned(Long id, User user) {
        Order order = findById(id);
        if (!order.belongsTo(user)) {
            throw new NotFoundException("Order #" + id + " was not found");
        }
        return order;
    }

    @Transactional(readOnly = true)
    public long countByStatus(OrderStatus status) {
        return orders.countByStatus(status);
    }

    /** Money taken from every order that was not cancelled. */
    @Transactional(readOnly = true)
    public BigDecimal totalRevenue() {
        return orders.findAll().stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Places an order from a form: either for an existing customer (admin page)
     * or for a shopper who typed their details (shop page). One transaction,
     * so a failed order does not leave a half-saved customer behind.
     */
    public Order placeOrder(OrderForm form) {
        if (form.getCustomerId() == null) {
            if (isBlank(form.getFullName()) || isBlank(form.getEmail()) || isBlank(form.getPhone())) {
                throw new IllegalArgumentException("Please fill in your name, email and phone");
            }
            if (!form.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                throw new IllegalArgumentException("Please enter a valid email address");
            }
        }
        Customer customer = form.getCustomerId() != null
                ? customerService.findById(form.getCustomerId())
                : customerService.findOrRegister(form.getFullName(), form.getEmail(), form.getPhone(), form.getAddress());
        return placeOrder(customer, form.getChosenQuantities(), form.getPickupDate(), form.getNotes());
    }

    /**
     * Creates an order and takes the items out of stock. If any product
     * cannot be supplied, nothing is saved.
     */
    public Order placeOrder(Customer customer, Map<Long, Integer> quantities, LocalDate pickupDate, String notes) {
        if (quantities.isEmpty()) {
            throw new IllegalArgumentException("Add at least one item to the order");
        }
        if (pickupDate != null && pickupDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Pickup date cannot be in the past");
        }
        Order order = new Order(customer, pickupDate, notes);
        quantities.forEach((productId, qty) -> order.addItem(productService.findById(productId), qty));
        return save(order);
    }

    public Order update(Long id, OrderStatus status, LocalDate pickupDate, String notes) {
        Order order = findById(id);
        order.changeStatus(status);
        order.setPickupDate(pickupDate);
        order.setNotes(notes);
        return save(order);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /** Deleting an open order puts its items back in stock. */
    @Override
    public void delete(Long id) {
        Order order = findById(id);
        order.returnItemsToStock();
        orders.delete(order);
    }
}
