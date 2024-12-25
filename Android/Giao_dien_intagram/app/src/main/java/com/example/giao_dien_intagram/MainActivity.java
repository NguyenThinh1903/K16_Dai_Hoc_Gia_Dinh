package com.example.giao_dien_intagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton; // Thêm nút đăng ký

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Thay đổi theo tên layout của bạn

        // Khởi tạo các thành phần
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton); // Khởi tạo nút đăng ký

        // Thêm sự kiện click cho nút đăng nhập
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });

        // Thêm sự kiện click cho nút đăng ký
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignupActivity(); // Mở SingupActivity đăng ký
            }
        });
    }

    private void checkLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Lấy thông tin đã lưu từ SharedPreferences (android.content)
        String savedUsername = getSharedPreferences("tk", MODE_PRIVATE)
                .getString("username", null);
        String savedPassword = getSharedPreferences("tk", MODE_PRIVATE)
                .getString("password", null);

        // Kiểm tra thông tin đăng nhập
        if (username.equals(savedUsername) && password.equals(savedPassword)) {
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            // Thực hiện hành động sau khi đăng nhập thành công (chuyển sang SingupActivity)
        } else {
            Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
        }
    }


    private void openSignupActivity() {
        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}
