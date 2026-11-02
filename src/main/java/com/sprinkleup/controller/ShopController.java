package com.sprinkleup.controller;

import com.sprinkleup.config.AuthInterceptor;
import com.sprinkleup.dto.OrderForm;
import com.sprinkleup.model.Customer;
import com.sprinkleup.model.User;
import com.sprinkleup.model.Order;
import com.sprinkleup.model.Product;
import com.sprinkleup.model.ProductType;
import com.sprinkleup.service.OrderService;
import com.sprinkleup.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/** The public side of the site: browsing the menu and placing an order. */
@Controller
public class ShopController {

    private final ProductService productService;
    private final OrderService orderService;

    public ShopController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String home(@RequestParam(required = false) ProductType type, Model model) {
        model.addAttribute("activePage", "shop");
        model.addAttribute("products", productService.findForShop(type));
        model.addAttribute("types", ProductType.values());
        model.addAttribute("selectedType", type);
        return "shop/home";
    }

    @GetMapping("/order")
    public String orderForm(@RequestParam(required = false) Long product, Model model) {
        OrderForm form = new OrderForm();
        if (product != null) {
            form.getQuantities().put(product, 1);
        }
        return showOrderForm(form, model);
    }

    @PostMapping("/order")
    public String placeOrder(@ModelAttribute("orderForm") OrderForm form,
                             @RequestAttribute(name = AuthInterceptor.CURRENT_USER, required = false) User currentUser,
                             Model model, RedirectAttributes redirect) {
        // Never trust a customer id sent by the browser: signed-in customers order as themselves, everyone else as a guest
        form.setCustomerId(currentUser instanceof Customer me ? me.getId() : null);
        try {
            Order order = orderService.placeOrder(form);
            redirect.addFlashAttribute("orderId", order.getId());
            return "redirect:/order/thanks";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return showOrderForm(form, model);
        }
    }

    @GetMapping("/order/thanks")
    public String thanks(Model model) {
        if (!(model.getAttribute("orderId") instanceof Long orderId)) {
            return "redirect:/"; // only reachable straight after placing an order
        }
        model.addAttribute("activePage", "order");
        model.addAttribute("order", orderService.findById(orderId));
        return "shop/thanks";
    }

    private String showOrderForm(OrderForm form, Model model) {
        List<Product> products = productService.findForShop(null).stream().filter(Product::isInStock).toList();
        model.addAttribute("activePage", "order");
        model.addAttribute("orderForm", form);
        model.addAttribute("products", products);
        return "shop/order";
    }
}
