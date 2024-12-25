package org.example.useCase.quanLyHoaDon_KS;

import org.example.ui.editInvoice.EditInvoiceView;
import org.example.useCase.addInvoice.AddInvoiceUIInputBoundary;
import org.example.useCase.deleteInvoice.DeleteInvoiceUIInputBoundary;
import org.example.useCase.deleteInvoice.DeleteInvoiceUIInputDTO;
import org.example.useCase.editInvoice.EditInvoiceInputBoundary;
import org.example.useCase.editInvoice.EditInvoiceInputDTO;
import org.example.useCase.editInvoice.EditInvoiceUIInputBoundary;
import org.example.useCase.editInvoice.EditInvoiceUIInputDTO;
import org.example.useCase.findInvoice.FindInvoiceUIInputBoundary;
import org.example.useCase.getListInvoice.GetListInvoiceUIInputBoundary;

import javax.swing.*;

public class QuanLyHDKhachSanUseCase implements QuanLyHDKhachSanInputBoundary {
    private AddInvoiceUIInputBoundary addInvoiceUIInputBoundary;
    private DeleteInvoiceUIInputBoundary deleteInvoiceUIInputBoundary;
    private FindInvoiceUIInputBoundary findInvoiceUIInputBoundary;
    private GetListInvoiceUIInputBoundary getListInvoiceUIInputBoundary;
    private QuanLyHDKhachSanOutputBoundary quanLyHDKhachSanOutputBoundary;
    private EditInvoiceUIInputBoundary editInvoiceUIInputBoundary;
    public QuanLyHDKhachSanUseCase(QuanLyHDKhachSanOutputBoundary quanLyHDKhachSanOutputBoundary) {
        this.quanLyHDKhachSanOutputBoundary = quanLyHDKhachSanOutputBoundary;
    }

    public void setAddInvoiceUIInputBoundary(AddInvoiceUIInputBoundary addInvoiceUIInputBoundary) {
        this.addInvoiceUIInputBoundary = addInvoiceUIInputBoundary;
    }

    @Override
    public void setEditInvoiceUIInputBoundary(EditInvoiceUIInputBoundary editInvoiceUIInputBoundary) {
        this.editInvoiceUIInputBoundary = editInvoiceUIInputBoundary;
    }

    public void setDeleteInvoiceUIInputBoundary(DeleteInvoiceUIInputBoundary deleteInvoiceUIInputBoundary) {
        this.deleteInvoiceUIInputBoundary = deleteInvoiceUIInputBoundary;
    }

    public void setGetListInvoiceUIInputBoundary(GetListInvoiceUIInputBoundary getListInvoiceUIInputBoundary) {
        this.getListInvoiceUIInputBoundary = getListInvoiceUIInputBoundary;
    }

    @Override
    public void SetFindInvoiceUIInputBoundary(FindInvoiceUIInputBoundary findInvoiceUIInputBoundary) {
    this.findInvoiceUIInputBoundary = findInvoiceUIInputBoundary;
    }

//    public void setFindInvoiceUIInputBoundary(FindInvoiceUIInputBoundary findInvoiceUIInputBoundary) {
//        this.findInvoiceUIInputBoundary = findInvoiceUIInputBoundary;
//    }

    @Override
    public void execute(QuanLyHDKhachSanInputDTO quanLyHDKhachSanInputDTO) {
        if (quanLyHDKhachSanInputDTO == null) {
            this.quanLyHDKhachSanOutputBoundary.exportError(new QuanLyHDKhachSanOutputDTO());
            return;
        }

        String chucNang = quanLyHDKhachSanInputDTO.getChucNang();
        QuanLyHDKhachSanOutputDTO quanLyHDKhachSanOutputDTO = new QuanLyHDKhachSanOutputDTO();

        switch (chucNang) {
            case "Thêm":
                if (this.addInvoiceUIInputBoundary != null) {
                    this.addInvoiceUIInputBoundary.execute();
                    quanLyHDKhachSanOutputDTO.setMessage("Thêm hóa đơn thành công.");
                } else {
                    quanLyHDKhachSanOutputDTO.setMessage("Không thể thêm hóa đơn.");
                }
                break;
            case "Xoá":
                if (this.deleteInvoiceUIInputBoundary != null) {
                    String maHD = quanLyHDKhachSanInputDTO.getMaHD();
                    if (maHD == null || maHD.isEmpty()){
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn thông tin cần xoá!",
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                    } else {
                        DeleteInvoiceUIInputDTO deleteInvoiceUIInputDTO = new DeleteInvoiceUIInputDTO();
                        deleteInvoiceUIInputDTO.setMaHD(maHD);
                        this.deleteInvoiceUIInputBoundary.execute(deleteInvoiceUIInputDTO);
                    }

                }
                break;
            case "Tìm kiếm":
                if (this.findInvoiceUIInputBoundary != null) {
                    this.findInvoiceUIInputBoundary.execute();
                    quanLyHDKhachSanOutputDTO.setMessage("Tìm kiếm hóa đơn thành công.");
                } else {
                    quanLyHDKhachSanOutputDTO.setMessage("Không thể tìm kiếm hóa đơn.");
                }
                break;
            case "Xuất":
                if (this.getListInvoiceUIInputBoundary != null) {
                    this.getListInvoiceUIInputBoundary.execute();
                    quanLyHDKhachSanOutputDTO.setMessage("Lấy danh sách hóa đơn thành công.");
                } else {
                    quanLyHDKhachSanOutputDTO.setMessage("Không thể lấy danh sách hóa đơn.");
                }
                break;
            case "Sửa":
                if (this.editInvoiceUIInputBoundary != null){
                    String maHD = quanLyHDKhachSanInputDTO.getMaHD();
                    if(maHD == null || maHD.isEmpty()){
                        JOptionPane.showMessageDialog(null,
                                "Vui lòng chọn hoá đơn cần sửa! ",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                         String tenKH = quanLyHDKhachSanInputDTO.getTenKh();
                         String maPhong = quanLyHDKhachSanInputDTO.getMaPhong();
                         String ngayHD = quanLyHDKhachSanInputDTO.getNgayHD();
                         String donGia = quanLyHDKhachSanInputDTO.getDonGia();
                         String loaiHoaDon = quanLyHDKhachSanInputDTO.getLoatHoaDon();
                         String soNgay = quanLyHDKhachSanInputDTO.getSoNgay();
                         String soGio = quanLyHDKhachSanInputDTO.getSoGio();
                        int response = JOptionPane.showConfirmDialog(null,
                                "Bạn có muốn sửa thông tin hóa đơn "+maHD+" không?",
                                "Xác nhận",
                                JOptionPane.YES_NO_OPTION);

                        if (response == JOptionPane.YES_OPTION) {
                            // Nếu người dùng chọn "Có", mở form EditInvoiceView
                            EditInvoiceView editInvoiceView = new EditInvoiceView();
                            EditInvoiceUIInputDTO editInvoiceUIInputDTO = new EditInvoiceUIInputDTO();
                            // Gán dữ liệu vào editInvoiceInputDTO
                            editInvoiceUIInputDTO.setMaHD(maHD);
                            editInvoiceUIInputDTO.setTenKH(tenKH);
                            editInvoiceUIInputDTO.setNgayHD(ngayHD);
                            editInvoiceUIInputDTO.setMaPhong(maPhong);
                            editInvoiceUIInputDTO.setDonGia(donGia);
                            editInvoiceUIInputDTO.setLoaiHoaDon(loaiHoaDon);
                            editInvoiceUIInputDTO.setSoNgay(soNgay);
                            editInvoiceUIInputDTO.setSoGio(soGio);

                            // Gọi phương thức để hiển thị form chỉnh sửa
                            editInvoiceUIInputBoundary.excute(editInvoiceUIInputDTO);
                        }
                    }
                }
            default:
                quanLyHDKhachSanOutputDTO.setMessage("Chức năng không hợp lệ.");
                break;
        }

        // Xuất kết quả
        this.quanLyHDKhachSanOutputBoundary.exportResult(quanLyHDKhachSanOutputDTO);
    }
}