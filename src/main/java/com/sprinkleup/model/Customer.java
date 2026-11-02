package com.sprinkleup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** A person who orders from SprinkleUp. */
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9+ ]{9,15}$", message = "Enter a valid phone number")
    @Column(nullable = false, length = 20)
    private String phone;

    @Size(max = 255)
    private String address;

    public Customer() {
    }

    public Customer(String fullName, String email, String phone, String address) {
        this.fullName = fullName;
        setEmail(email);
        this.phone = phone;
        this.address = address;
    }

    /** Copies the editable details from another customer object (used by the edit form). */
    public void updateDetails(Customer source) {
        this.fullName = source.fullName;
        setEmail(source.email);
        this.phone = source.phone;
        this.address = source.address;
    }

    @Override
    public String getDisplayName() {
        return fullName + " <" + email + ">";
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    /** Emails are stored in lower case so the same person is never saved twice. */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim().toLowerCase();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
