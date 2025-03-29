package com.coffeecup.coffeecup.controller;

import com.coffeecup.coffeecup.model.Product;
import com.coffeecup.coffeecup.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest; // *** THÊM IMPORT ***
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public String listProducts(Model model, HttpServletRequest request) { // *** THÊM THAM SỐ ***
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("currentURI", request.getRequestURI()); // *** THÊM VÀO MODEL ***
        return "product-list";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model, HttpServletRequest request) { // *** THÊM THAM SỐ ***
        Optional<Product> productOptional = productRepository.findById(id);
        model.addAttribute("currentURI", request.getRequestURI()); // *** THÊM VÀO MODEL ***
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            return "product-detail";
        } else {
            return "redirect:/products?error=notfound";
        }
    }
}