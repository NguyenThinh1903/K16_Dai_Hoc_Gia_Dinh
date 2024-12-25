package org.example.ui.getListInvoice;//package nhom4.ui.getListInvoice;

import org.example.useCase.getListInvoice.GetListInvoiceOutputBoundary;
import org.example.useCase.getListInvoice.GetListInvoiceOutputDTO;

import java.util.List;

public class GetListInvoicePresenter implements GetListInvoiceOutputBoundary {
    private final GetListInvoiceView getListInvoiceView;
    private final List<GetListInvoiceViewModel> listViewModel;

    public GetListInvoicePresenter(GetListInvoiceView getListInvoiceView, List<GetListInvoiceViewModel> getListInvoiceViewModel) {
        this.getListInvoiceView = getListInvoiceView;
        this.listViewModel = getListInvoiceViewModel;
    }

    @Override
    public void exportError(GetListInvoiceOutputDTO responseError) {
        this.listViewModel.clear();
        GetListInvoiceViewModel getListInvoiceViewModel = new GetListInvoiceViewModel(responseError.getStatus(), responseError.getMessage());

        if (this.getListInvoiceView != null) {
            this.getListInvoiceView.showMsgError(getListInvoiceViewModel);
        }
    }

    @Override
    public void exportResult(List<GetListInvoiceOutputDTO> listOutputDTO) {
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

        for (GetListInvoiceOutputDTO outputDTO : listOutputDTO) {
            maHD = String.valueOf(outputDTO.getMaHD());
            tenKH = String.valueOf(outputDTO.getTenKH());
            ngayHD = String.valueOf(outputDTO.getNgayHD());
            maPhong = String.valueOf(outputDTO.getMaPhong());
            donGia = String.valueOf(outputDTO.getDonGia());
            loaiHoaDon = String.valueOf(outputDTO.getLoaiHoaDon());
            soNgay = String.valueOf(outputDTO.getSoNgay());
            soGio = String.valueOf(outputDTO.getSoGio());
            thanhTien = String.valueOf(outputDTO.getThanhTien());

            this.listViewModel.add(new GetListInvoiceViewModel("success", "", maHD, tenKH, ngayHD, maPhong, donGia, loaiHoaDon, soNgay, soGio, thanhTien));
        }

        if (this.getListInvoiceView != null) {
//            this.getListInvoiceView.showResult(this.listViewModel);
        }
    }
}
