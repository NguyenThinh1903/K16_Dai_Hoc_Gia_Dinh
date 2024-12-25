package com.l221402.student.usecase;

import java.util.ArrayList;
import java.util.List;

import com.l221402.student.entity.Student;

public class GetStudentListUseCase implements GetStudentListInputBoundary{
    private GetStudentListOutputBoundary getSLOutputBoundary = null;
    private GetStudentListDatabaseBoundary getSLDBBoundary = null;

    

    public GetStudentListUseCase(GetStudentListOutputBoundary getSLOutputBoundary,
            GetStudentListDatabaseBoundary getSLDBBoundary) {
        this.getSLOutputBoundary = getSLOutputBoundary;
        this.getSLDBBoundary = getSLDBBoundary;
    }



    @Override
    public void execute() {

        //lay danh sach sin hvien
        List<Student> listStudent = getSLDBBoundary.getAllStudentList();
        List<GetStudentListOutputDTO> listOutDTO = new ArrayList<>();

        DataExport dataExport = new DataExport(listStudent);
        getSLOutputBoundary.exportResult(dataExport);

        for (Student student : listStudent) {

            GetStudentListOutputDTO studentDTO = new GetStudentListOutputDTO
            (student.getHoTen(), student.getNgaySinh(), student.getDiaChi(),
            student.getNganh(), student.tinhDiemTB(), student.getHocLuc()
            );

            listOutDTO.add(studentDTO);
            
        }

        getSLOutputBoundary.present(listOutDTO);


    }

}
