package com.gdu_springboot.dev_springboot.rest;


import com.gdu_springboot.dev_springboot.dao.StudentDAO;
import com.gdu_springboot.dev_springboot.entity.Student;
import com.gdu_springboot.dev_springboot.service.StudentService;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class StudentRestController {
   private StudentService studentService;

    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public List<Student> getStudents(){
        return studentService.findAll();
    }

    @GetMapping("/students/{id}")
    public Student getStudent(@PathVariable int id){
        return studentService.findById(id);
    }

    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student){
        student.setId(0);
        Student newStudent = studentService.save(student);
        return newStudent;
    }

    @PutMapping("/students")
    public Student updateStudent(@RequestBody Student student){
        Student newStudent = studentService.save(student);
        return newStudent;
    }

    @DeleteMapping("/students/{id}")
    public String deleteStudent(@PathVariable int id){
        studentService.deleteById(id);
        return "deleted success have id = " + id;
    }

}
