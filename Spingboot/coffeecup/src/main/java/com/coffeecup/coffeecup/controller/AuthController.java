package com.coffeecup.coffeecup.controller;

import com.coffeecup.coffeecup.model.Authority;
import com.coffeecup.coffeecup.model.User;
import com.coffeecup.coffeecup.repository.AuthorityRepository;
import com.coffeecup.coffeecup.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Kiểm tra nếu đã đăng nhập thì redirect về home
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
        //     return "redirect:/";
        // }
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("user") User user,
                                      BindingResult bindingResult,
                                      Model model, // Không cần Model ở đây vì dùng RedirectAttributes
                                      RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            // Giữ lại lỗi và trả về view register
            // model.addAttribute("user", user); // Không cần vì @ModelAttribute đã làm
            return "register";
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username already exists");
            // model.addAttribute("user", user); // Không cần
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Đảm bảo ROLE_USER tồn tại hoặc tạo mới
        Authority userRole = authorityRepository.findByName("ROLE_USER")
                .orElseGet(() -> authorityRepository.save(new Authority("ROLE_USER")));
        user.setAuthorities(Collections.singleton(userRole));

        user.setEnabled(true);
        userRepository.save(user);

        // Sử dụng Flash Attribute để thông báo không bị mất sau redirect
        redirectAttributes.addFlashAttribute("registrationSuccess", "Registration successful! Please login.");
        return "redirect:/login"; // Luôn redirect sau POST thành công
    }
}