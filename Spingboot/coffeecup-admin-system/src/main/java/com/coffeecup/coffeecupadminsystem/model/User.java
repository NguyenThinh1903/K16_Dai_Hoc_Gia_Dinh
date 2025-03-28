package com.coffeecup.coffeecupadminsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 4, max = 10, message = "Username must be between 4 and 6 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // Không dùng @Size ở đây vì lưu hash
    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @NotEmpty(message = "At least one role must be selected")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities = new HashSet<>();

    // Trường tạm thời nhận Confirm Password từ Form
    @Transient
    private String confirmPassword;

    // Trường tạm thời nhận Current Password từ Form
    @Transient
    private String currentPassword;

    // Đảm bảo authorities không null khi binding
    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = (authorities == null) ? new HashSet<>() : authorities;
    }
}