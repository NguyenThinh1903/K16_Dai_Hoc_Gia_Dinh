package org.example.entity;

import java.util.Date;

public class InvoiceNgay extends Invoice {
    private int soNgay;

    public InvoiceNgay(){

    }

    public InvoiceNgay(int maHD, String tenKH, Date ngayHoaDon, String maPhong, Double donGia, String loaiHoaDon, int SoNgay) {
        setMaHD(maHD);
        setTenKH(tenKH);
        setNgayHoaDon(ngayHoaDon);
        setMaPhong(maPhong);
        setDonGia(donGia);
        setLoaiHoaDon(loaiHoaDon);
        setSoNgay(SoNgay);
    }

    public int getSoNgay() {
        return soNgay;
    }

    public void setSoNgay(int soNgay) {
        this.soNgay = soNgay;
    }

    @Override
    public Double tinhThanhTien() {
        return soNgay * getDonGia();
    }

}

