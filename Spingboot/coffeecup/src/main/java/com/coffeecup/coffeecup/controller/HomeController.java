package com.coffeecup.coffeecup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home"; // Trả về file home.html
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact"; // Trang Contact
    }

    @GetMapping("/menu")
    public String menu() {
        return "menu"; // Trang Contact
    }

    @GetMapping("/about")
    public String about() {
        return "about"; // Trang Contact
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog"; // Trang Contact
    }

    @GetMapping("/shop")
    public String shop() {
        return "shop"; // Trang Contact
    }
}