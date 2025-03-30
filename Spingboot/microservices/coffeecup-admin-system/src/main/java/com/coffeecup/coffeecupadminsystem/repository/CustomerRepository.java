package com.coffeecup.coffeecupadminsystem.repository;

import com.coffeecup.coffeecupadminsystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional; // Import Optional

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Cần phương thức này để kiểm tra email trùng lặp
    Optional<Customer> findByEmail(String email);
}