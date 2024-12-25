package org.example.useCase.addInvoice;

public class AddInvoiceInputDTO {
    private String tenKH;
    private String ngayHoaDon;
    private String maPhong;
    private String loaiHoaDon;
    private String soGio;
    private String SoNgay;
    private String donGia;

    public AddInvoiceInputDTO() {
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getNgayHoaDon() {
        return ngayHoaDon;
    }

    public void setNgayHoaDon(String ngayHoaDon) {
       this.ngayHoaDon = ngayHoaDon;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getLoaiHoaDon() {
        return loaiHoaDon;
    }

    public void setLoaiHoaDon(String loaiHoaDon) {
        this.loaiHoaDon = loaiHoaDon;
    }

    public String getSoGio() {
        return soGio;
    }

    public void setSoGio(String soGio) {
        this.soGio = soGio;
    }

    public String getSoNgay() {
        return SoNgay;
    }

    public void setSoNgay(String soNgay) {
        SoNgay = soNgay;
    }

    public String getDonGia() {
        return donGia;
    }

    public void setDonGia(String donGia) {
        this.donGia = donGia;
    }
}
