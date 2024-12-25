package org.example.useCase.editInvoice;

import org.example.ui.editInvoice.EditInvoicePresenter;
import org.example.ui.editInvoice.EditInvoiceView;

public class EditInvoiceUIUseCase implements  EditInvoiceUIInputBoundary{
    private EditInvoiceUIPresenter editInvoiceUIPresenter;

    public EditInvoiceUIUseCase(EditInvoiceUIPresenter editInvoiceUIPresenter) {
        this.editInvoiceUIPresenter = editInvoiceUIPresenter;
    }

    @Override
    public void excute(EditInvoiceUIInputDTO editInvoiceUIInputDTO) {
        String maHD = editInvoiceUIInputDTO.getMaHD();
        String tenKH = editInvoiceUIInputDTO.getTenKH();
        String ngayHD = editInvoiceUIInputDTO.getNgayHD();
        String maPhong = editInvoiceUIInputDTO.getMaPhong();
        String donGia = editInvoiceUIInputDTO.getDonGia();
        String loaiHoaDon = editInvoiceUIInputDTO.getLoaiHoaDon();
        String soNgay = editInvoiceUIInputDTO.getSoNgay();
        String soGio = editInvoiceUIInputDTO.getSoGio();
        String thanhTien = editInvoiceUIInputDTO.getThanhTien();

        EditInvoiceUIOutptuDTO editInvoiceUIOutptuDTO = new EditInvoiceUIOutptuDTO();
        editInvoiceUIOutptuDTO.setMaHD(maHD);
        editInvoiceUIOutptuDTO.setTenKH(tenKH);
        editInvoiceUIOutptuDTO.setNgayHD(ngayHD);
        editInvoiceUIOutptuDTO.setMaPhong(maPhong);
        editInvoiceUIOutptuDTO.setDonGia(donGia);
        editInvoiceUIOutptuDTO.setLoaiHoaDon(loaiHoaDon);
        editInvoiceUIOutptuDTO.setSoNgay(soNgay);
        editInvoiceUIOutptuDTO.setSoGio(soGio);
        editInvoiceUIOutptuDTO.setThanhTien(thanhTien);
        editInvoiceUIPresenter.presenter(editInvoiceUIOutptuDTO);
    }
}
