package com.l221402.student.ui;

import com.l221402.student.usecase.GetStudentListInputBoundary;

public class GetStudentListController {
    private GetStudentListInputBoundary getStudentListInputBoundary = null;

    public GetStudentListController(GetStudentListInputBoundary getStudentListInputBoundary) {
        this.getStudentListInputBoundary = getStudentListInputBoundary;
    }

    public void execute() {
        getStudentListInputBoundary.execute();
    }

    

}
