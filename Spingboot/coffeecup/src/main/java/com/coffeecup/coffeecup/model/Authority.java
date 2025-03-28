package com.coffeecup.coffeecup.model;

import jakarta.persistence.*;
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

    @Column(nullable = false, unique = true, length = 50) // Thêm length cho MySQL
    private String name; // Ví dụ: "ROLE_USER", "ROLE_ADMIN"

    public Authority(String name) {
        this.name = name;
    }
}