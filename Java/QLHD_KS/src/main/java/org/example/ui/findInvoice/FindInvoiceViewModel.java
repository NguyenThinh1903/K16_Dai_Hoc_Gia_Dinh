package org.example.ui.findInvoice;

public class FindInvoiceViewModel {
    public String status;
    public String msg;
    public String maHD;
    public String tenKH;
    public String ngayHD;
    public String maPhong;
    public String donGia;
    public String loaiHoaDon;
    public String soNgay;
    public String soGio;
    public String thanhTien;

    public FindInvoiceViewModel(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public FindInvoiceViewModel(String status, String msg, String maHD, String tenKH, String ngayHD, String maPhong, String donGia,
                                String loaiHoaDon, String soNgay, String soGio, String thanhTien) {
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

    public FindInvoiceViewModel(String maHD, String tenKH, String ngayHD, String maPhong, String donGia,
                                String loaiHoaDon, String soNgay, String soGio, String thanhTien) {
        this.status = "success";
        this.msg = "";
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

    public FindInvoiceViewModel() {

    }


}
