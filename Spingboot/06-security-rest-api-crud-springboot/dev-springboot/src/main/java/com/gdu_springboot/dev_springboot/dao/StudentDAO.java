package com.gdu_springboot.dev_springboot.dao;

import com.gdu_springboot.dev_springboot.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.*;
public interface StudentDAO {
    Student getStudentById(int id);
    List<Student> getAllStudent();
    Student addStudent(Student student);
    void deleteStudentById(int id);
}
