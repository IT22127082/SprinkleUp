package com.sprinkleup.controller;

import com.sprinkleup.model.Customer;
import com.sprinkleup.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Admin CRUD for customers. */
@Controller
@RequestMapping("/admin/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ModelAttribute("activePage")
    public String activePage() {
        return "admin-customers";
    }

    // READ
    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("customers", customerService.search(q));
        model.addAttribute("q", q);
        return "admin/customers/list";
    }

    // CREATE
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "admin/customers/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("customer") Customer customer, BindingResult result,
                         RedirectAttributes redirect) {
        if (customer.getEmail() != null && customerService.emailTaken(customer.getEmail(), null)) {
            result.rejectValue("email", "duplicate", "A customer with this email already exists");
        }
        if (result.hasErrors()) {
            return "admin/customers/form";
        }
        customerService.create(customer);
        redirect.addFlashAttribute("success", customer.getFullName() + " was added.");
        return "redirect:/admin/customers";
    }

    // UPDATE
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        model.addAttribute("customerId", id);
        return "admin/customers/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("customer") Customer customer,
                         BindingResult result, RedirectAttributes redirect, Model model) {
        if (customer.getEmail() != null && customerService.emailTaken(customer.getEmail(), id)) {
            result.rejectValue("email", "duplicate", "Another customer already uses this email");
        }
        if (result.hasErrors()) {
            model.addAttribute("customerId", id);
            return "admin/customers/form";
        }
        customerService.update(id, customer);
        redirect.addFlashAttribute("success", customer.getFullName() + "'s details were updated.");
        return "redirect:/admin/customers";
    }

    // DELETE
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            customerService.delete(id);
            redirect.addFlashAttribute("success", "Customer was deleted.");
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/customers";
    }
}
