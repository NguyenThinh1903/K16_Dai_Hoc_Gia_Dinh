package com.gdu.dev_springboot_demo.controller;

import com.gdu.dev_springboot_demo.model.Student;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StudentController {
    @GetMapping("/student-form")
    public String studentForm(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);
        return "student-form";
    }
    @PostMapping("/process-student-form")
    public String processStudentForm(@Valid @ModelAttribute("student") Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "student-form";
        }else {
            return "process-student-form";
        }
    }
}
