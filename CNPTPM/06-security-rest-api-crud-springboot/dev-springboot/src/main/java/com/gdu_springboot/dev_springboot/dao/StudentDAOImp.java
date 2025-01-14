package com.gdu_springboot.dev_springboot.dao;

import com.gdu_springboot.dev_springboot.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class StudentDAOImp implements StudentDAO{
    @Autowired
    public StudentDAOImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private EntityManager entityManager;
    @Override
    public Student getStudentById(int id) {
        return entityManager.find(Student.class, id);
    }

    @Override
    public List<Student> getAllStudent() {
        TypedQuery<Student> query = entityManager.createQuery("select s from Student s", Student.class);
        List<Student> students = query.getResultList();

        return students;
    }

    @Override
    @Transactional
    public Student addStudent(Student student) {
        Student newStudent = entityManager.merge(student);
        return newStudent;
    }

    @Override
    @Transactional
    public void deleteStudentById(int id) {
        entityManager.remove(entityManager.find(Student.class, id));
    }
}
