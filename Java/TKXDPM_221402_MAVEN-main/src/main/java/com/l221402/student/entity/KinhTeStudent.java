package com.l221402.student.entity;

import java.util.Date;

public class KinhTeStudent extends Student {
    private double diemMarketing;
    private double diemSales;

    public KinhTeStudent(String hoTen, Date ngaySinh, String diaChi, double diemMarketing, double diemSales) {
        super(hoTen, "KT", ngaySinh, diaChi);
        this.diemMarketing = diemMarketing;
        this.diemSales = diemSales;
    }

    @Override
    public double tinhDiemTB() {
        return (2 * diemMarketing + diemSales) / 3;
    }
}