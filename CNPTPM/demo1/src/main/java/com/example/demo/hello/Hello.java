package com.example.demo.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    private static final String GREETING_MESSAGE = "<h1>Hello</h1>";
    private static final String SHOP_MESSAGE = "<h2>Shop</h2>";

    @GetMapping("/")
    public String hello() {
        return GREETING_MESSAGE;
    }

    @GetMapping("/shop")
    public String shop() {
        return SHOP_MESSAGE;
    }
}