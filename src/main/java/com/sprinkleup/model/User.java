package com.sprinkleup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Anyone known to the bakery (ABSTRACTION). Customer and Admin extend it
 * (INHERITANCE) and say where they go after signing in (POLYMORPHISM).
 *
 * The password is stored only as a BCrypt hash, and there is no getter for it:
 * other classes can only ask "is this password right?" (INFORMATION HIDING).
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User extends BaseEntity {

    public static final int MIN_PASSWORD_LENGTH = 6;

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

    /** Null when the person has never set up an online account (e.g. added by staff). */
    @Column(length = 100)
    private String passwordHash;

    protected User() {
    }

    protected User(String fullName, String email, String phone) {
        setFullName(fullName);
        setEmail(email);
        setPhone(phone);
    }

    /** What kind of user this is. */
    public abstract UserRole getRole();

    /** The page this user lands on after signing in. */
    public abstract String getHomePath();

    public boolean isAdmin() {
        return getRole() == UserRole.ADMIN;
    }

    public boolean hasAccount() {
        return passwordHash != null;
    }

    public void setPassword(String rawPassword, PasswordEncoder encoder) {
        if (rawPassword == null || rawPassword.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
        this.passwordHash = encoder.encode(rawPassword);
    }

    public boolean checkPassword(String rawPassword, PasswordEncoder encoder) {
        return passwordHash != null && rawPassword != null && encoder.matches(rawPassword, passwordHash);
    }

    @Override
    public String getDisplayName() {
        return fullName + " <" + email + ">";
    }

    public String getFirstName() {
        return fullName == null ? "" : fullName.trim().split("\\s+")[0];
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName == null ? null : fullName.trim();
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
        this.phone = phone == null ? null : phone.trim();
    }
}
