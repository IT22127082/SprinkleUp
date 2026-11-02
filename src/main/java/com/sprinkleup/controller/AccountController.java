package com.sprinkleup.controller;

import com.sprinkleup.config.AuthInterceptor;
import com.sprinkleup.dto.ProfileForm;
import com.sprinkleup.model.AbstractOrder;
import com.sprinkleup.model.Customer;
import com.sprinkleup.service.AuthService;
import com.sprinkleup.service.CustomOrderService;
import com.sprinkleup.service.CustomerService;
import com.sprinkleup.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** A signed-in customer's own page: their orders, requests and details. */
@Controller
@RequestMapping("/account")
public class AccountController {

    private final OrderService orderService;
    private final CustomOrderService customOrderService;
    private final CustomerService customerService;
    private final AuthService authService;

    public AccountController(OrderService orderService, CustomOrderService customOrderService,
                             CustomerService customerService, AuthService authService) {
        this.orderService = orderService;
        this.customOrderService = customOrderService;
        this.customerService = customerService;
        this.authService = authService;
    }

    @ModelAttribute("activePage")
    public String activePage() {
        return "account";
    }

    @GetMapping
    public String home(@RequestAttribute(AuthInterceptor.CURRENT_USER) Customer me, Model model) {
        // Menu orders and custom cakes in one list: each draws itself through AbstractOrder (POLYMORPHISM)
        List<AbstractOrder> history = new ArrayList<>();
        history.addAll(orderService.findByCustomer(me));
        history.addAll(customOrderService.findByCustomer(me));
        history.sort(Comparator.comparing(AbstractOrder::getCreatedAt).reversed());
        model.addAttribute("history", history);
        return "account/home";
    }

    @GetMapping("/orders/{id}")
    public String order(@PathVariable Long id, @RequestAttribute(AuthInterceptor.CURRENT_USER) Customer me, Model model) {
        model.addAttribute("order", orderService.findOwned(id, me));
        return "account/order";
    }

    @GetMapping("/edit")
    public String editForm(@RequestAttribute(AuthInterceptor.CURRENT_USER) Customer me, Model model) {
        model.addAttribute("profileForm", ProfileForm.from(me));
        return "account/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("profileForm") ProfileForm form, BindingResult result,
                       @RequestAttribute(AuthInterceptor.CURRENT_USER) Customer me, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "account/edit";
        }
        customerService.updateProfile(me.getId(), form);
        redirect.addFlashAttribute("success", "Your details were saved.");
        return "redirect:/account";
    }

    @PostMapping("/password")
    public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 @RequestAttribute(AuthInterceptor.CURRENT_USER) Customer me, RedirectAttributes redirect) {
        try {
            authService.changePassword(me.getId(), currentPassword, newPassword, confirmPassword);
            redirect.addFlashAttribute("success", "Your password was changed.");
            return "redirect:/account";
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/account/edit";
        }
    }
}
