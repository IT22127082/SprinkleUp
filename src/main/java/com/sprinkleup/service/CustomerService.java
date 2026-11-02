package com.sprinkleup.service;

import com.sprinkleup.model.Customer;
import com.sprinkleup.repository.CustomerRepository;
import com.sprinkleup.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService extends AbstractCrudService<Customer> {

    private final CustomerRepository customers;
    private final OrderRepository orders;

    public CustomerService(CustomerRepository customers, OrderRepository orders) {
        super(customers);
        this.customers = customers;
        this.orders = orders;
    }

    @Override
    protected String entityName() {
        return "Customer";
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customers.findAllByOrderByFullNameAsc();
    }

    @Transactional(readOnly = true)
    public List<Customer> search(String query) {
        if (query == null || query.isBlank()) {
            return findAll();
        }
        String q = query.trim();
        return customers.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByFullNameAsc(q, q);
    }

    @Transactional(readOnly = true)
    public boolean emailTaken(String email, Long exceptId) {
        return exceptId == null
                ? customers.existsByEmailIgnoreCase(email)
                : customers.existsByEmailIgnoreCaseAndIdNot(email, exceptId);
    }

    public Customer create(Customer customer) {
        return save(customer);
    }

    public Customer update(Long id, Customer details) {
        Customer customer = findById(id);
        customer.updateDetails(details);
        return save(customer);
    }

    /** Finds a shopper by email, or registers them (used by the public order page). */
    public Customer findOrRegister(String fullName, String email, String phone, String address) {
        return customers.findByEmailIgnoreCase(email.trim())
                .map(existing -> {
                    existing.updateDetails(new Customer(fullName, email, phone,
                            address == null || address.isBlank() ? existing.getAddress() : address));
                    return existing;
                })
                .orElseGet(() -> save(new Customer(fullName, email, phone, address)));
    }

    /** Customers with orders are kept so the order history stays complete. */
    @Override
    public void delete(Long id) {
        Customer customer = findById(id);
        if (orders.existsByCustomer(customer)) {
            throw new IllegalStateException(customer.getFullName()
                    + " has orders. Delete their orders first.");
        }
        customers.delete(customer);
    }
}
