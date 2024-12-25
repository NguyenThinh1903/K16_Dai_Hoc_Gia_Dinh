package org.example.useCase.getListInvoice;

import java.util.Date;

public class GetListInvoiceOutputDTO {
    private String status;
    private String message;
    private int maHD;
    private String tenKH;
    private Date ngayHD;
    private String maPhong;
    private double donGia;
    private String loaiHoaDon;
    private int soNgay;
    private int soGio;
    private double thanhTien;

    public GetListInvoiceOutputDTO() {
    }

    public GetListInvoiceOutputDTO( int maHD, String tenKH, Date ngayHD, String maPhong, double donGia, String loaiHoaDon, int soNgay, int soGio, double thanhTien) {
        this.maHD = maHD;
        this.tenKH = tenKH;
        this.ngayHD = ngayHD;
        this.maPhong = maPhong;
        this.donGia = donGia;
        this.loaiHoaDon = loaiHoaDon;
        this.soNgay = soNgay;
        this.soGio = soGio;
        this.thanhTien = thanhTien;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public Date getNgayHD() {
        return ngayHD;
    }

    public void setNgayHD(Date ngayHD) {
        this.ngayHD = ngayHD;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getLoaiHoaDon() {
        return loaiHoaDon;
    }

    public void setLoaiHoaDon(String loaiHoaDon) {
        this.loaiHoaDon = loaiHoaDon;
    }

    public int getSoNgay() {
        return soNgay;
    }

    public void setSoNgay(int soNgay) {
        this.soNgay = soNgay;
    }

    public int getSoGio() {
        return soGio;
    }

    public void setSoGio(int soGio) {
        this.soGio = soGio;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }
}
