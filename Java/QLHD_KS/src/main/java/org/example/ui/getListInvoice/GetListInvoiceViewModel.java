package org.example.ui.getListInvoice;

/*
 * ViewModel hoàn toàn là chuỗi (String, boolean) [boolean: đặt biến cờ]
 * View chỉ lấy dữ liệu và xuất lên màn hình [check theo biến cờ để xuất lên màn hình cho phù hợp]
 * Presenter xử lý [khởi tạo biến cờ vào ViewModel]
 *
 */

public class GetListInvoiceViewModel {
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

    public GetListInvoiceViewModel(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public GetListInvoiceViewModel(String status, String msg, String maHD, String tenKH, String ngayHD, String maPhong, String donGia,
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

    public GetListInvoiceViewModel(String maHD, String tenKH, String ngayHD, String maPhong, String donGia,
                                   String loaiHoaDon, String soNgay, String soGio, String thanhTien) {
        this.status = "success";
        this.msg = "";
        this.maPhong = maPhong;
        this.tenKH = tenKH;
        this.ngayHD = ngayHD;
        this.maPhong = maPhong;
        this.donGia = donGia;
        this.loaiHoaDon = loaiHoaDon;
        this.soNgay = soNgay;
        this.soGio = soGio;
        this.thanhTien = thanhTien;
    }

    public GetListInvoiceViewModel() {

    }

}
