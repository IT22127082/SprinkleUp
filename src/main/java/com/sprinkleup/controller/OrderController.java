package com.sprinkleup.controller;

import com.sprinkleup.dto.OrderForm;
import com.sprinkleup.model.Order;
import com.sprinkleup.model.OrderStatus;
import com.sprinkleup.model.Product;
import com.sprinkleup.service.CustomerService;
import com.sprinkleup.service.OrderService;
import com.sprinkleup.service.ProductService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/** Admin CRUD for orders. */
@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;

    public OrderController(OrderService orderService, CustomerService customerService, ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @ModelAttribute("activePage")
    public String activePage() {
        return "admin-orders";
    }

    @ModelAttribute("statuses")
    public OrderStatus[] statuses() {
        return OrderStatus.values();
    }

    // READ
    @GetMapping
    public String list(@RequestParam(required = false) OrderStatus status, Model model) {
        model.addAttribute("orders", orderService.findByStatus(status));
        model.addAttribute("selectedStatus", status);
        return "admin/orders/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        return "admin/orders/view";
    }

    // CREATE
    @GetMapping("/new")
    public String newForm(@RequestParam(required = false) Long customer, Model model) {
        OrderForm form = new OrderForm();
        form.setCustomerId(customer);
        return showForm(form, model);
    }

    @PostMapping
    public String create(@ModelAttribute("orderForm") OrderForm form, Model model, RedirectAttributes redirect) {
        if (form.getCustomerId() == null) {
            model.addAttribute("error", "Choose a customer");
            return showForm(form, model);
        }
        try {
            Order order = orderService.placeOrder(form);
            redirect.addFlashAttribute("success", "Order #" + order.getId() + " was created.");
            return "redirect:/admin/orders/" + order.getId();
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return showForm(form, model);
        }
    }

    // UPDATE
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @RequestParam OrderStatus status,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate pickupDate,
                         @RequestParam(required = false) String notes, RedirectAttributes redirect) {
        try {
            orderService.update(id, status, pickupDate, notes);
            redirect.addFlashAttribute("success", "Order #" + id + " was updated.");
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    // DELETE
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        orderService.delete(id);
        redirect.addFlashAttribute("success", "Order #" + id + " was deleted.");
        return "redirect:/admin/orders";
    }

    private String showForm(OrderForm form, Model model) {
        model.addAttribute("orderForm", form);
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("products", productService.findForShop(null).stream().filter(Product::isInStock).toList());
        return "admin/orders/form";
    }
}
