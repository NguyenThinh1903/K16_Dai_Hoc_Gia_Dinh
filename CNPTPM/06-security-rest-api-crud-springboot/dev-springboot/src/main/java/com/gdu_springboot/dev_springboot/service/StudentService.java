package com.gdu_springboot.dev_springboot.service;

import com.gdu_springboot.dev_springboot.entity.Student;

import java.util.*;

public interface StudentService {
    List<Student> findAll();

    Student findById(int id);
    Student save(Student student);
    void deleteById(int id);

}
