package com.coffeecup.coffeecupadminsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern; // Import Pattern
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Server-side: Cho phép chữ cái Unicode (bao gồm tiếng Việt)
    @NotEmpty(message = "Customer name cannot be empty")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Name must contain only letters, spaces, and typical name punctuation (.',-)")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Size(max = 100)
    @Column(length = 100, unique = true) // unique=true để DB kiểm tra thêm
    private String email;

    @NotEmpty(message = "Phone cannot be empty")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    @Size(max = 10, min = 10)
    @Column(length = 10)
    private String phone;

    @NotEmpty(message = "Address cannot be empty")
    @Size(max = 255)
    private String address;
}