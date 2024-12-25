package org.example.entity;

import java.util.Date;

public class InvoiceGio extends Invoice {
    private int soGio;

    public InvoiceGio() {

    }

    public InvoiceGio(int maHD, String tenKH, Date ngayHoaDon, String maPhong, Double donGia, String loaiHoaDon, int soGio) {
        setMaHD(maHD);
        setTenKH(tenKH);
        setNgayHoaDon(ngayHoaDon);
        setMaPhong(maPhong);
        setDonGia(donGia);
        setLoaiHoaDon(loaiHoaDon);
        setSoGio(soGio);
    }

    public int getSoGio() {
        return soGio;
    }

    public void setSoGio(int soGio) {
        this.soGio = soGio;
    }

    @Override
    public Double tinhThanhTien() {
        return soGio* getDonGia();
    }
}
