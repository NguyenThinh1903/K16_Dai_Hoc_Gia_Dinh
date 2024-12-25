package com.l221402.student.database;

import java.util.List;

import com.l221402.student.entity.Student;
import com.l221402.student.usecase.GetStudentListDatabaseBoundary;

public class GetStudentListDAOMemory implements GetStudentListDatabaseBoundary {

    private List<Student> mockStudentDB = null;

        
    public GetStudentListDAOMemory(List<Student> mockStudentDB) {
        this.mockStudentDB = mockStudentDB;
    }



    @Override
    public List<Student> getAllStudentList() {
        return this.mockStudentDB;
    }

}
