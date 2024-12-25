package com.l221402.student.ui;

import java.awt.Color;

public class GetStudentListViewModel {
    public String stt;
    public String hoTen;
    public String diaChi;
    public String ngaySinh;//dd/MM/yyy
    public String diemTB;//%.1f
    public String hocLuc;//yeu = do
    public String nganh;
    public Color textColor;  // For color styling
    public boolean bold;     // For bold text
    public boolean italic;   // For italic text
    
    
    public GetStudentListViewModel(String stt, String hoTen, String diaChi, String ngaySinh, String diemTB,
            String hocLuc, String nganh, Color textColor, boolean bold, boolean italic) {
        this.stt = stt;
        this.hoTen = hoTen;
        this.diaChi = diaChi;
        this.ngaySinh = ngaySinh;
        this.diemTB = diemTB;
        this.hocLuc = hocLuc;
        this.nganh = nganh;
        this.textColor = textColor;
        this.bold = bold;
        this.italic = italic;
    }

}
