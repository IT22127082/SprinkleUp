package com.sprinkleup.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/** A member of the bakery staff who can use the admin pages. */
@Entity
@Table(name = "admins")
public class Admin extends User {

    private String jobTitle;

    protected Admin() {
    }

    public Admin(String fullName, String email, String phone, String jobTitle) {
        super(fullName, email, phone);
        this.jobTitle = jobTitle;
    }

    @Override
    public UserRole getRole() {
        return UserRole.ADMIN;
    }

    @Override
    public String getHomePath() {
        return "/admin";
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
