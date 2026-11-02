package com.sprinkleup.controller;

import com.sprinkleup.model.CustomOrderStatus;
import com.sprinkleup.service.CustomOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

/** Bakery side of custom cake requests: list, view, quote, move along, delete. */
@Controller
@RequestMapping("/admin/custom-orders")
public class CustomOrderAdminController {

    private final CustomOrderService customOrderService;

    public CustomOrderAdminController(CustomOrderService customOrderService) {
        this.customOrderService = customOrderService;
    }

    @ModelAttribute("activePage")
    public String activePage() {
        return "admin-custom";
    }

    @ModelAttribute("statuses")
    public CustomOrderStatus[] statuses() {
        return CustomOrderStatus.values();
    }

    @GetMapping
    public String list(@RequestParam(required = false) CustomOrderStatus status, Model model) {
        model.addAttribute("requests", customOrderService.findByStatus(status));
        model.addAttribute("selectedStatus", status);
        return "admin/custom/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("order", customOrderService.findById(id));
        return "admin/custom/view";
    }

    @PostMapping("/{id}/quote")
    public String quote(@PathVariable Long id, @RequestParam(required = false) BigDecimal price,
                        @RequestParam(required = false) String note, RedirectAttributes redirect) {
        try {
            customOrderService.quote(id, price, note);
            redirect.addFlashAttribute("success", "Quote sent to the customer.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/custom-orders/" + id;
    }

    @PostMapping("/{id}/status")
    public String status(@PathVariable Long id, @RequestParam CustomOrderStatus status, RedirectAttributes redirect) {
        try {
            customOrderService.changeStatus(id, status);
            redirect.addFlashAttribute("success", "Request #" + id + " is now \"" + status.getLabel() + "\".");
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/custom-orders/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        customOrderService.delete(id);
        redirect.addFlashAttribute("success", "Custom request #" + id + " was deleted.");
        return "redirect:/admin/custom-orders";
    }
}
