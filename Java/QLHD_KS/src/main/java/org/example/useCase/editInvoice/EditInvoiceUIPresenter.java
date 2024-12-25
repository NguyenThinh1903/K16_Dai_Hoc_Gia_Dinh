package org.example.useCase.editInvoice;

import org.example.ui.editInvoice.EditInvoiceView;
import org.example.ui.editInvoice.EditInvoiceViewModel;

public class EditInvoiceUIPresenter {
    private EditInvoiceViewModel editInvoiceViewModel;
    private EditInvoiceView editInvoiceView;

    public EditInvoiceUIPresenter(EditInvoiceViewModel editInvoiceViewModel, EditInvoiceView editInvoiceView) {
        this.editInvoiceViewModel = editInvoiceViewModel;
        this.editInvoiceView = editInvoiceView;
    }

    public void presenter(EditInvoiceUIOutptuDTO editInvoiceUIOutptuDTO){
        String maHD = editInvoiceUIOutptuDTO.getMaHD();
        String tenKH = editInvoiceUIOutptuDTO.getTenKH();
        String ngayHD = editInvoiceUIOutptuDTO.getNgayHD();
        String maPhong = editInvoiceUIOutptuDTO.getMaPhong();
        String donGia = editInvoiceUIOutptuDTO.getDonGia();
        String loaiHoaDon = editInvoiceUIOutptuDTO.getLoaiHoaDon();
        String soNgay = editInvoiceUIOutptuDTO.getSoNgay();
        String soGio = editInvoiceUIOutptuDTO.getSoGio();
        String thanhTien = editInvoiceUIOutptuDTO.getThanhTien();

        editInvoiceViewModel.maHD = maHD;
        editInvoiceViewModel.tenKH = tenKH;
        editInvoiceViewModel.ngayHD = ngayHD;
        editInvoiceViewModel.maPhong = maPhong;
        editInvoiceViewModel.donGia = donGia;
        editInvoiceViewModel.loaiHoaDon= loaiHoaDon;
        editInvoiceViewModel.soNgay = soNgay;
        editInvoiceViewModel.soGio = soGio;
        editInvoiceViewModel.thanhTien = thanhTien;
        this.editInvoiceView.showInvocie(editInvoiceViewModel);
    }
}
