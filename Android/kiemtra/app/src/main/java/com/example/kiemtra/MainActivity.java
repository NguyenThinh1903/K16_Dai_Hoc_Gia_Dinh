package com.example.kiemtra;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // set datepicker to 2020
        DatePicker datePicker = findViewById(R.id.datepicker);
        datePicker.updateDate(2020, Calendar.JANUARY, 1);

        Button dangKyBtn = findViewById(R.id.dangKyBtn);
        dangKyBtn.setOnClickListener(v -> xuLyDangKy());
    }

    private void xuLyDangKy() {
        EditText fullName = findViewById(R.id.fullName);
        EditText mssv = findViewById(R.id.mssv);
        DatePicker datePicker = findViewById(R.id.datepicker);
        CheckBox checkboxBongDa = findViewById(R.id.checkboxBongDa);
        CheckBox checkboxNhacKich = findViewById(R.id.checkboxNhacKich);
        CheckBox checkboxKyThuat = findViewById(R.id.checkboxKyThuat);
        CheckBox checkboxVanHoc = findViewById(R.id.checkboxVanHoc);
        EditText lyDoThamGia = findViewById(R.id.lyDoThamGia);

        String fullNameStr = fullName.getText().toString().trim();
        String mssvStr = mssv.getText().toString().trim();
        int ngay = datePicker.getDayOfMonth();
        int thang = datePicker.getMonth() + 1;
        int nam = datePicker.getYear();
        String lyDoThamGiaStr = lyDoThamGia.getText().toString().trim();

        // Kiểm tra họ và tên
        if (fullNameStr.length() < 12) {
            showMsg("Lỗi", "Họ và tên phải có ít nhất 12 ký tự");
            return;
        }

        // Kiểm tra MSSV
        if (mssvStr.length() != 8 || !mssvStr.matches("\\d{8}")) {
            showMsg("Lỗi", "MSSV phải có 8 ký tự và phải là số");
            return;
        }

        // Kiểm tra ngày đăng ký
        if (nam < 2020 || (nam == 2020 && thang == 1 && ngay <= 1)) {
            showMsg("Lỗi", "Ngày đăng ký phải sau ngày 1/1/2020");
            return;
        }

        // Kiểm tra câu lạc bộ
        StringBuilder cauLacBoStr = new StringBuilder();
        if (checkboxBongDa.isChecked()) {
            cauLacBoStr.append(checkboxBongDa.getText()).append(", ");
        }
        if (checkboxNhacKich.isChecked()) {
            cauLacBoStr.append(checkboxNhacKich.getText()).append(", ");
        }
        if (checkboxKyThuat.isChecked()) {
            cauLacBoStr.append(checkboxKyThuat.getText()).append(", ");
        }
        if (checkboxVanHoc.isChecked()) {
            cauLacBoStr.append(checkboxVanHoc.getText()).append(", ");
        }

        if (cauLacBoStr.length() == 0) {
            showMsg("Lỗi", "Phải chọn ít nhất 1 câu lạc bộ");
            return;
        } else {
            cauLacBoStr = new StringBuilder(cauLacBoStr.substring(0, cauLacBoStr.length() - 2));
        }

        // Kiểm tra lý do tham gia
        if (lyDoThamGiaStr.isEmpty()) {
            lyDoThamGiaStr = "không có";
        }

        // Thông báo
        showMsg("Thông tin đăng ký câu lạc bộ",
                "Họ và tên: " + fullNameStr + "\n" +
                        "MSSV: " + mssvStr + "\n" +
                        "Ngày đăng ký: " + ngay + "/" + thang + "/" + nam + "\n" +
                        "Câu lạc bộ: " + cauLacBoStr + "\n" +
                        "Lý do tham gia: " + lyDoThamGiaStr + "\n"
        );
    }

    private void showMsg(String title, String msg) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}