package com.sprinkleup.controller;

import com.sprinkleup.config.AuthInterceptor;
import com.sprinkleup.dto.CustomOrderForm;
import com.sprinkleup.model.CustomOrder;
import com.sprinkleup.model.Customer;
import com.sprinkleup.service.CustomOrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Customer side of custom cake requests: create, view, edit, accept a quote, cancel. */
@Controller
@RequestMapping("/custom-orders")
public class CustomOrderController {

    private final CustomOrderService customOrderService;

    public CustomOrderController(CustomOrderService customOrderService) {
        this.customOrderService = customOrderService;
    }

    @ModelAttribute("activePage")
    public String activePage() {
        return "custom";
    }

    @ModelAttribute
    public void formOptions(Model model) {
        model.addAttribute("occasions", CustomOrderForm.OCCASIONS);
        model.addAttribute("flavours", CustomOrderForm.FLAVOURS);
        model.addAttribute("earliestDate", CustomOrder.earliestDate());
        model.addAttribute("minDaysNotice", CustomOrder.MIN_DAYS_NOTICE);
    }

    // CREATE
    @GetMapping("/new")
    public String newForm(Model model) {
        CustomOrderForm form = new CustomOrderForm();
        form.setNeededBy(CustomOrder.earliestDate());
        model.addAttribute("customOrderForm", form);
        return "custom/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("customOrderForm") CustomOrderForm form, BindingResult result,
                         @RequestAttribute(AuthInterceptor.CURRENT_USER) Customer me,
                         Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "custom/form";
        }
        try {
            CustomOrder order = customOrderService.request(me, form);
            redirect.addFlashAttribute("success", "Your request was sent! We'll reply with a quote soon.");
            return "redirect:/custom-orders/" + order.getId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "custom/form";
        }
    }

    // READ
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, @RequestAttribute(AuthInterceptor.CURRENT_USER) Customer me, Model model) {
        model.addAttribute("order", customOrderService.findOwned(id, me));
        return "custom/view";
    }

    // UPDATE
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, @RequestAttribute(AuthInterceptor.CURRENT_USER) Customer me,
                           Model model, RedirectAttributes redirect) {
        CustomOrder order = customOrderService.findOwned(id, me);
        if (!order.isEditable()) {
            redirect.addFlashAttribute("error", "This request already has a quote, so it can't be edited.");
            return "redirect:/custom-orders/" + id;
        }
        model.addAttribute("customOrderForm", order.toForm());
        model.addAttribute("orderId", id);
        return "custom/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("customOrderForm") CustomOrderForm form,
                         BindingResult result, @RequestAttribute(AuthInterceptor.CURRENT_USER) Customer me,
                         Model model, RedirectAttributes redirect) {
        model.addAttribute("orderId", id);
        if (result.hasErrors()) {
            return "custom/form";
        }
        try {
            customOrderService.updateRequest(id, me, form);
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "custom/form";
        }
        redirect.addFlashAttribute("success", "Your request was updated.");
        return "redirect:/custom-orders/" + id;
    }

    @PostMapping("/{id}/accept")
    public String accept(@PathVariable Long id, @RequestAttribute(AuthInterceptor.CURRENT_USER) Customer me,
                         RedirectAttributes redirect) {
        try {
            customOrderService.accept(id, me);
            redirect.addFlashAttribute("success", "Quote accepted! We'll start planning your cake.");
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/custom-orders/" + id;
    }

    // DELETE (a customer cancels rather than erasing, so the bakery keeps a record)
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, @RequestAttribute(AuthInterceptor.CURRENT_USER) Customer me,
                         RedirectAttributes redirect) {
        try {
            customOrderService.cancel(id, me);
            redirect.addFlashAttribute("success", "Your request was cancelled.");
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/custom-orders/" + id;
    }
}
