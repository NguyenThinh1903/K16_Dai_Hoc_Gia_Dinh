package org.example.ui.findInvoice;

import org.example.useCase.findInvoice.FindInvoiceOutputBoundary;
import org.example.useCase.findInvoice.FindInvoiceOutputDTO;

import java.util.List;

public class FindInvoicePresenter implements FindInvoiceOutputBoundary {
    private final FindInvoiceView findInvoiceView;
    private final List<FindInvoiceViewModel> listViewModel;

    public FindInvoicePresenter(FindInvoiceView findInvoiceView, List<FindInvoiceViewModel> listViewModel) {
        this.findInvoiceView = findInvoiceView;
        this.listViewModel = listViewModel;
    }

    @Override
    public void exportResult(List<FindInvoiceOutputDTO> listInvoice) {
        this.listViewModel.clear();

        String maHD;
        String tenKH;
        String ngayHD;
        String maPhong;
        String donGia;
        String loaiHoaDon;
        String soNgay;
        String soGio;
        String thanhTien;

        for (FindInvoiceOutputDTO outputDTO : listInvoice) {
            maHD = String.valueOf(outputDTO.getMaHD());
            tenKH = String.valueOf(outputDTO.getTenKH());
            ngayHD = String.valueOf(outputDTO.getNgayHD());
            maPhong = String.valueOf(outputDTO.getMaPhong());
            donGia = String.valueOf(outputDTO.getDonGia());
            loaiHoaDon = String.valueOf(outputDTO.getLoaiHoaDon());
            soNgay = String.valueOf(outputDTO.getSoNgay());
            soGio = String.valueOf(outputDTO.getSoGio());
            thanhTien = String.valueOf(outputDTO.getThanhTien());

            this.listViewModel.add(new FindInvoiceViewModel("success", "", maHD, tenKH, ngayHD, maPhong, donGia, loaiHoaDon, soNgay, soGio, thanhTien));
        }

        if (this.findInvoiceView != null) {
            this.findInvoiceView.showResult(this.listViewModel);
        }
    }

    @Override
    public void exportError(FindInvoiceOutputDTO responseError) {
        this.listViewModel.clear();
        FindInvoiceViewModel findInvoiceViewModel = new FindInvoiceViewModel(responseError.getStatus(), responseError.getMessage());

        if (this.findInvoiceView != null) {
            this.findInvoiceView.showMsgError(findInvoiceViewModel);
        }
    }
}
