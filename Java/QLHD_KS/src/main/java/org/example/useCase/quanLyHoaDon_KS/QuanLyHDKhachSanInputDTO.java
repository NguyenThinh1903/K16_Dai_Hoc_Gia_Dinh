package org.example.useCase.quanLyHoaDon_KS;

public class QuanLyHDKhachSanInputDTO {
    private String chucNang;
    private String maHD;
    private String tenKh;
    private String ngayHD;
    private String maPhong;
    private  String loatHoaDon;
    private String soNgay;
    private String soGio;
    private String thanhTien;
    private String donGia;

    public String getMaHD() {
        return maHD;
    }

    public String getDonGia() {
        return donGia;
    }

    public void setDonGia(String donGia) {
        this.donGia = donGia;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getTenKh() {
        return tenKh;
    }

    public void setTenKh(String tenKh) {
        this.tenKh = tenKh;
    }

    public String getNgayHD() {
        return ngayHD;
    }

    public void setNgayHD(String ngayHD) {
        this.ngayHD = ngayHD;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getLoatHoaDon() {
        return loatHoaDon;
    }

    public void setLoatHoaDon(String loatHoaDon) {
        this.loatHoaDon = loatHoaDon;
    }

    public String getSoNgay() {
        return soNgay;
    }

    public void setSoNgay(String soNgay) {
        this.soNgay = soNgay;
    }

    public String getSoGio() {
        return soGio;
    }

    public void setSoGio(String soGio) {
        this.soGio = soGio;
    }

    public String getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(String thanhTien) {
        this.thanhTien = thanhTien;
    }

    public QuanLyHDKhachSanInputDTO() {
    }

    public String getChucNang() {
        return chucNang;
    }

    public void setChucNang(String chucNang) {
        this.chucNang = chucNang;
    }
}