package com.example.checkbox;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CheckBox checkKemBo, checkKemSauRieng, checkKemDau;
    private RadioGroup radioGroupSize;
    private TextView textKemChon, textKichCo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các thành phần giao diện
        checkKemBo = findViewById(R.id.checkKemBo);
        checkKemSauRieng = findViewById(R.id.checkKemSauRieng);
        checkKemDau = findViewById(R.id.checkKemDau);
        radioGroupSize = findViewById(R.id.radioGroupSize);
        textKemChon = findViewById(R.id.textKemChon); // Vẫn giữ dòng này
        textKichCo = findViewById(R.id.textKichCo); // Vẫn giữ dòng này

        // Thiết lập lắng nghe sự thay đổi cho các hộp kiểm
        checkKemBo.setOnCheckedChangeListener((buttonView, isChecked) -> updateResult());
        checkKemSauRieng.setOnCheckedChangeListener((buttonView, isChecked) -> updateResult());
        checkKemDau.setOnCheckedChangeListener((buttonView, isChecked) -> updateResult());

        // Thiết lập lắng nghe sự thay đổi cho RadioGroup
        radioGroupSize.setOnCheckedChangeListener((group, checkedId) -> updateResult());
    }

    private void updateResult() {
        StringBuilder kemChon = new StringBuilder("Kem đã chọn: ");

        // Kiểm tra các loại kem đã chọn
        if (checkKemBo.isChecked()) {
            kemChon.append("Kem bơ ");
        }
        if (checkKemSauRieng.isChecked()) {
            kemChon.append("Kem sầu riêng ");
        }
        if (checkKemDau.isChecked()) {
            kemChon.append("Kem dâu ");
        }

        // Hiển thị loại kem đã chọn
        textKemChon.setText(kemChon.toString());

        // Kiểm tra kích thước đã chọn
        int selectedSizeId = radioGroupSize.getCheckedRadioButtonId();
        if (selectedSizeId != -1) { // Kiểm tra có một nút radio được chọn không
            RadioButton selectedSize = findViewById(selectedSizeId);
            textKichCo.setText("Kích thước: " + selectedSize.getText());
        } else {
            textKichCo.setText("Kích thước: Chọn kích thước");
        }
    }
}
