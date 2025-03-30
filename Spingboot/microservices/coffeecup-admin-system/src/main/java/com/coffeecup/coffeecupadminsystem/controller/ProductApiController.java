package com.coffeecup.coffeecupadminsystem.controller;

import com.coffeecup.coffeecupadminsystem.model.Product;
import com.coffeecup.coffeecupadminsystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Dùng ResponseEntity để kiểm soát response tốt hơn
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; // *** Dùng RestController ***

import java.util.List;
import java.util.Optional;

@RestController // *** Đánh dấu đây là REST Controller ***
@RequestMapping("/api/products") // *** Tiền tố chung cho API sản phẩm ***
public class ProductApiController {

    @Autowired
    private ProductRepository productRepository;

    // --- API LẤY DANH SÁCH SẢN PHẨM ---
    @GetMapping
    public List<Product> getAllProducts() {
        // Trả về trực tiếp List<Product>, Spring Boot sẽ tự chuyển thành JSON
        return productRepository.findAll();
    }

    // --- API LẤY CHI TIẾT SẢN PHẨM ---
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        // Dùng ResponseEntity để trả về 200 OK nếu tìm thấy, 404 Not Found nếu không
        return productOptional
                .map(ResponseEntity::ok) // Nếu có, trả về status 200 và product trong body
                .orElseGet(() -> ResponseEntity.notFound().build()); // Nếu không, trả về status 404
    }

    // --- CÁC API KHÁC (POST, PUT, DELETE) SẼ THÊM SAU NẾU CẦN PHÁT TRIỂN SAU  ---

}