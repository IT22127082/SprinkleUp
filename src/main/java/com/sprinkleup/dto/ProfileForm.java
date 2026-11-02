package com.sprinkleup.dto;

import com.sprinkleup.model.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** The details a customer can change on their own account page. */
public class ProfileForm {

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String fullName;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9+ ]{9,15}$", message = "Enter a valid phone number")
    private String phone;

    @Size(max = 255)
    private String address;

    public static ProfileForm from(Customer customer) {
        ProfileForm form = new ProfileForm();
        form.fullName = customer.getFullName();
        form.phone = customer.getPhone();
        form.address = customer.getAddress();
        return form;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
