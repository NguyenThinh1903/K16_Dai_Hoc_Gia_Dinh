package com.gdu.dev_springboot_demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("/hello-form")
    public String helloForm() {
        return "hello-form";
    }
    @GetMapping("/processHelloForm")
    public String processHelloForm(HttpServletRequest request, Model model) {
        //doc form
        String theName = request.getParameter("theName");
        //xu ly gia tri nhan
        theName = theName.toUpperCase();
        //them gia tri vao model
        model.addAttribute("theName", theName);
        return "processHelloForm";
    }
}
