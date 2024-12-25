package org.example.ui.quanLyHDKhachSan;

import org.example.entity.Invoice;
import org.example.useCase.quanLyHoaDon_KS.QuanLyHDKhachSanOutputBoundary;
import org.example.useCase.quanLyHoaDon_KS.QuanLyHDKhachSanOutputDTO;

import java.util.List;

public class QuanLyHDKhachSanPresenter implements QuanLyHDKhachSanOutputBoundary {
    private QuanLyHDKhachSanViewModel quanLyHDKhachSanViewModel;
    private QuanLyHDKhachSanView quanLyHDKhachSanView;

    public QuanLyHDKhachSanPresenter(QuanLyHDKhachSanViewModel quanLyHDKhachSanViewModel, QuanLyHDKhachSanView quanLyHDKhachSanView) {
        this.quanLyHDKhachSanViewModel = quanLyHDKhachSanViewModel;
        this.quanLyHDKhachSanView = quanLyHDKhachSanView;
    }

    @Override
    public void exportError(QuanLyHDKhachSanOutputDTO quanLyHDKhachSanOutputDTO) {
        this.quanLyHDKhachSanViewModel.status = "error";
        this.quanLyHDKhachSanViewModel.message = quanLyHDKhachSanOutputDTO.getMessage();

        if (this.quanLyHDKhachSanView == null){
            this.quanLyHDKhachSanView.showMesageError(this.quanLyHDKhachSanViewModel);
        }
    }

    @Override
    public void exportResult(QuanLyHDKhachSanOutputDTO quanLyHDKhachSanOutputDTO) {
        this.quanLyHDKhachSanViewModel.status = "success";
        this.quanLyHDKhachSanViewModel.message = quanLyHDKhachSanOutputDTO.getMessage();
        if (this.quanLyHDKhachSanView != null){
            this.quanLyHDKhachSanView.showResult(this.quanLyHDKhachSanViewModel);
        }
    }
}
