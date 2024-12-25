package com.l221402.student.ui;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.l221402.student.usecase.DataExport;
import com.l221402.student.usecase.GetStudentListOutputBoundary;
import com.l221402.student.usecase.GetStudentListOutputDTO;
import com.l221402.student.usecase.ResponseData;

public class GetStudentListPresenter implements GetStudentListOutputBoundary{
    private DataExport dataExport = null;
    private List<GetStudentListOutputDTO> listOutputDTO = null;
    private List<GetStudentListViewModel> listViewModels =null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void exportResult(ResponseData responseData) {
        this.dataExport = (DataExport)responseData;
    }

    public DataExport getDataExport() {
        return dataExport;
    }

    @Override
    public void present(List<GetStudentListOutputDTO> listOutDTO) {
        this.listOutputDTO = listOutDTO;
        listViewModels = new ArrayList<>();
        int index = 1;
        for (GetStudentListOutputDTO studentDTO : listOutDTO) {
            
    
        String hocLuc = studentDTO.getHocLuc();
            Color color = Color.BLACK;
            boolean bold = false;
            boolean italic = false;

             // Set style based on "Học lực"
             switch (hocLuc) {
                case "Yếu":
                    color = Color.RED;
                    break;
                case "Xuất sắc":
                    bold = true;
                    italic = true;
                    break;
                case "Giỏi":
                    bold = true;
                    break;
                default:
                    break;
            }

            String stt = String.valueOf(index++);
            String ngaySinh = dateFormat.format(studentDTO.getNgaySinh());
            String diemTB = String.format("%.1f", studentDTO.getDiemTB());

            GetStudentListViewModel viewModel = new GetStudentListViewModel
            
            (stt, studentDTO.getHoTen(),
                    studentDTO.getDiaChi(), ngaySinh, diemTB, studentDTO.getHocLuc(), studentDTO.getNganh(), color,
                    bold, italic);

            listViewModels.add(viewModel);
        }


        GetStudentListView form = new GetStudentListView();
        //form.createAndShowGUI(listOutDTO);
        form.createAndShowGUI3(listViewModels);
    }

    public List<GetStudentListOutputDTO> getListOutputDTO() {
        return listOutputDTO;
    }

}
