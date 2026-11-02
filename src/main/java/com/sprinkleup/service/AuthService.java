package com.sprinkleup.service;

import com.sprinkleup.dto.RegisterForm;
import com.sprinkleup.model.Customer;
import com.sprinkleup.model.User;
import com.sprinkleup.repository.CustomerRepository;
import com.sprinkleup.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/** Signing in, signing up and changing passwords. */
@Service
@Transactional
public class AuthService {

    private final UserRepository users;
    private final CustomerRepository customers;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository users, CustomerRepository customers, PasswordEncoder encoder) {
        this.users = users;
        this.customers = customers;
        this.encoder = encoder;
    }

    /** Returns the user if the email and password match; the error never says which one was wrong. */
    @Transactional(readOnly = true)
    public User signIn(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Enter your email and password");
        }
        return users.findByEmailIgnoreCase(email.trim())
                .filter(user -> user.checkPassword(password, encoder))
                .orElseThrow(() -> new IllegalArgumentException("Wrong email or password"));
    }

    /**
     * Creates a customer account. If this email already ordered as a guest,
     * that customer record gets the password, so their past orders show up.
     */
    public Customer register(RegisterForm form) {
        if (!form.passwordsMatch()) {
            throw new IllegalArgumentException("The two passwords don't match");
        }
        Optional<User> existing = users.findByEmailIgnoreCase(form.getEmail().trim());
        Customer customer;
        if (existing.isEmpty()) {
            customer = new Customer(form.getFullName(), form.getEmail(), form.getPhone(), form.getAddress());
        } else if (existing.get() instanceof Customer guest && !guest.hasAccount()) {
            customer = guest;
            customer.setFullName(form.getFullName());
            customer.setPhone(form.getPhone());
            if (form.getAddress() != null && !form.getAddress().isBlank()) {
                customer.setAddress(form.getAddress());
            }
        } else {
            throw new IllegalArgumentException("An account with this email already exists. Please sign in.");
        }
        customer.setPassword(form.getPassword(), encoder);
        return customers.save(customer);
    }

    public void changePassword(Long userId, String currentPassword, String newPassword, String confirmPassword) {
        User user = users.findById(userId).orElseThrow();
        if (!user.checkPassword(currentPassword, encoder)) {
            throw new IllegalArgumentException("Your current password is not correct");
        }
        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("The two new passwords don't match");
        }
        user.setPassword(newPassword, encoder);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUser(Long id) {
        return users.findById(id);
    }
}
