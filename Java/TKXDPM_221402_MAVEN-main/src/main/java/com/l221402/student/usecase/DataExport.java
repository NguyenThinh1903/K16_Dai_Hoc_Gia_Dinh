package com.l221402.student.usecase;

import java.util.List;

import com.l221402.student.entity.Student;

public class DataExport implements ResponseData{
    private List<Student> list = null;

    public DataExport(List<Student> list) {
        this.list = list;
    }

    public List<Student> getList() {
        return list;
    }

}
