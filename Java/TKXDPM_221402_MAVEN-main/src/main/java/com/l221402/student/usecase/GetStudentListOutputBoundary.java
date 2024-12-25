package com.l221402.student.usecase;

import java.util.List;

public interface GetStudentListOutputBoundary {

    void exportResult(ResponseData responseData );

    void present(List<GetStudentListOutputDTO> listOutDTO);

}
