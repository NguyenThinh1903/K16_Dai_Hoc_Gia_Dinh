package com.gdu_crud_demo.crud_demo.dao;

import com.gdu_crud_demo.crud_demo.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentADOImp implements StudentDAO {
    private EntityManager entityManager;

    public StudentADOImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Student student) {
        entityManager.persist(student);
    }

    @Override
    @Transactional
    public void update(Student student) {
        entityManager.merge(student);
    }

    @Override
    @Transactional
    public void delete(int id) {
        Student student = entityManager.find(Student.class, id);
        entityManager.remove(student);
    }

    @Override
    public Student findById(int id) {
        return entityManager.find(Student.class, id);
    }

    @Override
    public List<Student> findAll() {
        TypedQuery<Student> query = entityManager.createQuery(" select s from Student s ",Student.class);
        return query.getResultList();
    }

    @Override
    public List<Student> findByName(String name) {
        TypedQuery<Student> query = entityManager.createQuery(" select s from Student s where s.firstName =:theName ",Student.class);
        query.setParameter("theName", name);
        return query.getResultList();
    }

    @Override
    @Transactional
    public int deleteAll() {
        int numberOfRowsDeleted = entityManager.createQuery(" delete from Student ").executeUpdate();
        return numberOfRowsDeleted;
    }
}
