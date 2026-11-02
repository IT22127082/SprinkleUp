package com.sprinkleup.controller;

import com.sprinkleup.model.CustomOrderStatus;
import com.sprinkleup.model.OrderStatus;
import com.sprinkleup.service.CustomOrderService;
import com.sprinkleup.service.CustomerService;
import com.sprinkleup.service.OrderService;
import com.sprinkleup.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ProductService productService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final CustomOrderService customOrderService;

    public DashboardController(ProductService productService, CustomerService customerService,
                               OrderService orderService, CustomOrderService customOrderService) {
        this.productService = productService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.customOrderService = customOrderService;
    }

    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("activePage", "admin-dashboard");
        model.addAttribute("productCount", productService.count());
        model.addAttribute("lowStockCount", productService.countLowStock());
        model.addAttribute("customerCount", customerService.count());
        model.addAttribute("orderCount", orderService.count());
        model.addAttribute("openOrders", orderService.countByStatus(OrderStatus.PENDING)
                + orderService.countByStatus(OrderStatus.BAKING)
                + orderService.countByStatus(OrderStatus.READY));
        model.addAttribute("revenue", orderService.totalRevenue().add(customOrderService.confirmedRevenue()));
        model.addAttribute("awaitingQuote", customOrderService.countByStatus(CustomOrderStatus.REQUESTED));
        model.addAttribute("customCount", customOrderService.count());
        model.addAttribute("recentOrders", orderService.findRecent());
        return "admin/dashboard";
    }
}
