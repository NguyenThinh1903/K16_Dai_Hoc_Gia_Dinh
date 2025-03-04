package com.gdu_springboot.dev_springboot.rest;

import com.gdu_springboot.dev_springboot.entity.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentRestController {

    private List<Student> students;

    @PostConstruct
    private void init() {
        students = new ArrayList<>();
        students.add(new Student(1, "Truong", "Tran", "truong@gmail.com"));
        students.add(new Student(2, "Tran", "Truong", "tran@gmail.com"));
        students.add(new Student(3, "Van", "Sang", "sang@gmail.com"));
        students.add(new Student(4, "Nguyen", "Tuan", "tuan@gmail.com"));
        students.add(new Student(5, "Chien", "Thang", "thang@gmail.com"));
        students.add(new Student(6, "Van", "Khanh", "khanh@gmail.com"));
    }

    @GetMapping("/students")
    public List<Student> getStudents() {
        return students;
    }
    @GetMapping("/students/{id}")
    public Student getStudent(@PathVariable int id) {
        return students.get(id);
    }
}
