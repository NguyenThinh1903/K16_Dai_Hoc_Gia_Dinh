package com.example.giao_dien_intagram;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup); // Tạo layout cho activity này

        // Khởi tạo các thành phần
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        // Thêm sự kiện click cho nút đăng ký
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();
            }
        });
    }
    private void registerAccount() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Kiểm tra thông tin đăng ký
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập và mật khẩu!", Toast.LENGTH_SHORT).show();
        } else {
            // Lưu thông tin tài khoản mới vào SharedPreferences( có sẵn trong android.content)
            getSharedPreferences("tk", MODE_PRIVATE)
                    .edit()
                    .putString("username", username)
                    .putString("password", password)
                    .apply();

            Toast.makeText(this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
            finish(); // Đóng activity Signup đăng ký sau khi tạo tài khoản
        }
    }
}
