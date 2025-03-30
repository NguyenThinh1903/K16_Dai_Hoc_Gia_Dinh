package com.coffeecup.coffeecup.repository; // Sửa package nếu khác

import com.coffeecup.coffeecup.model.Product; // Sửa import nếu cần
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}