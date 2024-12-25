package com.example.demo.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    @GetMapping("/")
    public String hello() {
        return "<h1>Hello</h1>";
    }

    @GetMapping("/shop")
    public String shop() {
        return "<h2>Shop</h2>";
    }
}
