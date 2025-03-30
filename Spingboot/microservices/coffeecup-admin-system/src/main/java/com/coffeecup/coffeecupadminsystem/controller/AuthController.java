package com.coffeecup.coffeecupadminsystem.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginForm() {
        // Kiểm tra nếu đã đăng nhập thì redirect về trang chủ của admin system
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            // Người dùng đã đăng nhập, chuyển về trang chủ thay vì hiển thị lại login
            return "redirect:/";
        }
        // Nếu chưa đăng nhập, hiển thị form login
        return "login";
    }

    // Bạn có thể thêm trang register ở đây nếu cần cho admin system, nhưng đề bài không yêu cầu
}