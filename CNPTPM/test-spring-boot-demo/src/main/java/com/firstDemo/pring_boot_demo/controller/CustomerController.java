package com.firstDemo.pring_boot_demo.controller;

import com.firstDemo.pring_boot_demo.model.Customer;
import jakarta.validation.Valid; // Import the @Valid annotation
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class CustomerController {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
    @GetMapping("/customer-form")
    public String xinChao(@ModelAttribute("customer") Customer customer, Model model) {
        // Don't create a new Customer here.  Thymeleaf will create it and bind the values.
        List<String> countries = Arrays.asList("", "USA", "Canada", "United Kingdom", "Australia", "Germany", "India");
        model.addAttribute("countries", countries);
        return "customerApplicationForm";
    }

    @PostMapping("/processCustomerForm")
    public String processCustomerForm(
            @Valid @ModelAttribute("customer") Customer customer, // Add @Valid here
            BindingResult bindingResult,
            Model model // Add model if you need to pass data to the view
    ) {
        if (bindingResult.hasErrors()) {
            return xinChao(customer, model);
        } else {
            return "processCustomerForm";
        }
    }
}