package com.coffeecup.coffeecup.controller;

import jakarta.servlet.http.HttpServletRequest; // *** THÊM IMPORT ***
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // *** THÊM IMPORT ***
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) { // *** THÊM THAM SỐ ***
        model.addAttribute("currentURI", request.getRequestURI()); // *** THÊM VÀO MODEL ***
        return "home";
    }

    @GetMapping("/contact")
    public String contact(HttpServletRequest request, Model model) { // *** THÊM THAM SỐ ***
        model.addAttribute("currentURI", request.getRequestURI()); // *** THÊM VÀO MODEL ***
        return "contact";
    }

    @GetMapping("/menu")
    public String menu(HttpServletRequest request, Model model) { // *** THÊM THAM SỐ ***
        model.addAttribute("currentURI", request.getRequestURI()); // *** THÊM VÀO MODEL ***
        return "menu";
    }

    @GetMapping("/about")
    public String about(HttpServletRequest request, Model model) { // *** THÊM THAM SỐ ***
        model.addAttribute("currentURI", request.getRequestURI()); // *** THÊM VÀO MODEL ***
        return "about";
    }

    @GetMapping("/blog")
    public String blog(HttpServletRequest request, Model model) { // *** THÊM THAM SỐ ***
        model.addAttribute("currentURI", request.getRequestURI()); // *** THÊM VÀO MODEL ***
        return "blog";
    }

    @GetMapping("/shop")
    public String shop(HttpServletRequest request, Model model) { // *** THÊM THAM SỐ ***
        model.addAttribute("currentURI", request.getRequestURI()); // *** THÊM VÀO MODEL ***
        return "shop";
    }
}