package com.sprinkleup.controller;

import com.sprinkleup.dto.ProductForm;
import com.sprinkleup.model.ProductType;
import com.sprinkleup.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Admin CRUD for products. */
@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ModelAttribute("activePage")
    public String activePage() {
        return "admin-products";
    }

    @ModelAttribute("types")
    public ProductType[] types() {
        return ProductType.values();
    }

    // READ
    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) ProductType type, Model model) {
        model.addAttribute("products", productService.search(q, type));
        model.addAttribute("q", q);
        model.addAttribute("selectedType", type);
        model.addAttribute("lowStockLimit", ProductService.LOW_STOCK_LIMIT);
        return "admin/products/list";
    }

    // CREATE
    @GetMapping("/new")
    public String newForm(@RequestParam(defaultValue = "CAKE") ProductType type, Model model) {
        ProductForm form = new ProductForm();
        form.setType(type);
        type.newProduct().fillSpecs(form); // start with the type's default specs
        model.addAttribute("productForm", form);
        return "admin/products/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("productForm") ProductForm form, BindingResult result,
                         RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            return "admin/products/form";
        }
        try {
            productService.create(form);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/products/form";
        }
        redirect.addFlashAttribute("success", "\"" + form.getName() + "\" was added to the menu.");
        return "redirect:/admin/products";
    }

    // UPDATE
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("productForm", ProductForm.from(productService.findById(id)));
        return "admin/products/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("productForm") ProductForm form,
                         BindingResult result, RedirectAttributes redirect, Model model) {
        form.setId(id);
        if (result.hasErrors()) {
            return "admin/products/form";
        }
        try {
            productService.update(id, form);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/products/form";
        }
        redirect.addFlashAttribute("success", "\"" + form.getName() + "\" was updated.");
        return "redirect:/admin/products";
    }

    // DELETE
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        String name = productService.findById(id).getName();
        productService.delete(id);
        redirect.addFlashAttribute("success", "\"" + name + "\" was removed from the menu.");
        return "redirect:/admin/products";
    }
}
