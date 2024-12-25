package org.example.useCase.quanLyHoaDon_KS;

public interface QuanLyHDKhachSanOutputBoundary {
    void exportError(QuanLyHDKhachSanOutputDTO quanLyHDKhachSanOutputDTO);
    void exportResult(QuanLyHDKhachSanOutputDTO quanLyHDKhachSanOutputDTO);
}