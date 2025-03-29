package com.coffeecup.coffeecup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size; // *** THÊM IMPORT NÀY ***
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ma;

    @NotEmpty(message = "Product name cannot be empty")
    @Size(max = 255) // *** THÊM SIZE CHO TÊN ***
    @Column(nullable = false)
    private String ten;

    @Size(max = 500) // Giới hạn mô tả
    @Column(length = 500)
    private String moTa;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String noiDung;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.01", message = "Price must be positive") // Sửa lại value > 0
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal gia;

    // --- THÊM TRƯỜNG LƯU TÊN FILE ẢNH ---
    @Column(length = 255, nullable = true) // Cho phép null nếu sản phẩm không có ảnh
    private String imageFileName;

    // --- THÊM THUỘC TÍNH TIỆN ÍCH (không lưu vào DB) ---
    // Để xây dựng đường dẫn ảnh trong Thymeleaf dễ dàng hơn
    @Transient
    public String getImageUrl() {
        if (imageFileName == null || imageFileName.isEmpty()) {
            return "/images/placeholder.png"; // Ảnh mặc định từ static
        }
        // URL path đã được map trong MvcConfig
        return "/uploads/products/" + imageFileName;
    }
}