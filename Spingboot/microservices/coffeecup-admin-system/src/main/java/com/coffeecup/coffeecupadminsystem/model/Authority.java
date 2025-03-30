package com.coffeecup.coffeecupadminsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty; // Import
import jakarta.validation.constraints.Pattern; // Import
import jakarta.validation.constraints.Size; // Import
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "authorities")
@Getter
@Setter
@NoArgsConstructor
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- THÊM VALIDATION ---
    @NotEmpty(message = "Role name cannot be empty")
    @Size(max = 50, message= "Role name cannot exceed 50 characters")
    @Pattern(regexp = "^[A-Z_]+$", message = "Role name must contain only uppercase letters and underscores (e.g., ROLE_MANAGER)")
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    public Authority(String name) {
        this.name = name;
    }
}