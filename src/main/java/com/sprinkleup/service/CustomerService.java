package com.sprinkleup.service;

import com.sprinkleup.dto.ProfileForm;
import com.sprinkleup.model.Customer;
import com.sprinkleup.repository.CustomOrderRepository;
import com.sprinkleup.repository.CustomerRepository;
import com.sprinkleup.repository.OrderRepository;
import com.sprinkleup.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService extends AbstractCrudService<Customer> {

    private final CustomerRepository customers;
    private final UserRepository users;
    private final OrderRepository orders;
    private final CustomOrderRepository customOrders;

    public CustomerService(CustomerRepository customers, UserRepository users,
                           OrderRepository orders, CustomOrderRepository customOrders) {
        super(customers);
        this.customers = customers;
        this.users = users;
        this.orders = orders;
        this.customOrders = customOrders;
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

    /** Checks every user (customers and staff), since emails must be unique across both. */
    @Transactional(readOnly = true)
    public boolean emailTaken(String email, Long exceptId) {
        return exceptId == null
                ? users.existsByEmailIgnoreCase(email)
                : users.existsByEmailIgnoreCaseAndIdNot(email, exceptId);
    }

    public Customer create(Customer customer) {
        return save(customer);
    }

    public Customer update(Long id, Customer details) {
        Customer customer = findById(id);
        customer.updateDetails(details);
        return save(customer);
    }

    /** A signed-in customer changing their own details (email stays the same). */
    public Customer updateProfile(Long id, ProfileForm form) {
        Customer customer = findById(id);
        customer.setFullName(form.getFullName());
        customer.setPhone(form.getPhone());
        customer.setAddress(form.getAddress());
        return save(customer);
    }

    /**
     * Finds a guest shopper by email, or adds them (used by the public order page).
     * Emails that belong to an online account must sign in instead, so a
     * stranger can't order as someone else or overwrite their details.
     */
    public Customer findOrRegister(String fullName, String email, String phone, String address) {
        users.findByEmailIgnoreCase(email.trim()).ifPresent(user -> {
            if (user.hasAccount() || user.isAdmin()) {
                throw new IllegalArgumentException("This email belongs to a SprinkleUp account. Please sign in to place your order.");
            }
        });
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
        if (orders.existsByCustomer(customer) || customOrders.existsByCustomer(customer)) {
            throw new IllegalStateException(customer.getFullName()
                    + " has orders. Delete their orders first.");
        }
        customers.delete(customer);
    }
}
