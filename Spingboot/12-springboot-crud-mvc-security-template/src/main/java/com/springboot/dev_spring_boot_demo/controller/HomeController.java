package com.springboot.dev_spring_boot_demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/shop")
        public String shop() {
            return "shop";
        }

        @GetMapping("/about")
    public String about() {
        return "about";
        }

        @GetMapping("/services")
    public String services() {
        return "services";
        }

        @GetMapping("/blog")
    public String blog() {
        return "blog";
        }

        @GetMapping("/contact")
    public String contact() {
        return "contact";
        }


}
