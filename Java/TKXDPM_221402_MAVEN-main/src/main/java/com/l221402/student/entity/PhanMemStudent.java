package com.l221402.student.entity;

import java.util.Date;

public class PhanMemStudent extends Student {

    private double diemJava;
    private double diemCss;
    private double diemHtml;

    public PhanMemStudent(String hoTen, Date ngaySinh, String diaChi, double diemJava, double diemCss, double diemHtml) {
        super(hoTen, "PM", ngaySinh, diaChi);
        this.diemJava = diemJava;
        this.diemCss = diemCss;
        this.diemHtml = diemHtml;
    }

    @Override
    public double tinhDiemTB() {
        return (2 * diemJava + diemHtml + diemCss) / 4;
    }
}
