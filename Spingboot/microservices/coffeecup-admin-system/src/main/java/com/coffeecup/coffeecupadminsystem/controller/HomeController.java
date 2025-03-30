package com.coffeecup.coffeecupadminsystem.controller; // Sửa package nếu cần

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class HomeController {

    // Trang chủ mặc định sau khi login thành công
    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        model.addAttribute("isAdmin", authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        model.addAttribute("isSystem", authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_SYSTEM")));
        return "index"; // Tạo file index.html
    }
}