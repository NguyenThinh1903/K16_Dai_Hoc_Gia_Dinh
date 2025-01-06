package com.example.lab_1;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // WelcomeActivity
    private void welcomeActivity() {
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    // LoginActivity
    private void loginActivity() {
        setContentView(R.layout.activity_login);

        EditText edtUsername = findViewById(R.id.editText);
        EditText edtPassword = findViewById(R.id.editText1);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Your login logic here
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    // RegisterActivity
    private void registerActivity() {
        setContentView(R.layout.activity_register);

        EditText edtEmail = findViewById(R.id.editText);
        EditText edtUsername = findViewById(R.id.editText2);
        EditText edtPassword = findViewById(R.id.editText3);
        EditText edtConfirm = findViewById(R.id.editText4);

        Button btnSignIn = findViewById(R.id.btnLogin);
        Button btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // InfoActivity
    private void infoActivity() {
        setContentView(R.layout.activity_info);

        EditText edtName = findViewById(R.id.editText4);
        EditText edtEmail = findViewById(R.id.editText);
        EditText edtUsername = findViewById(R.id.editText2);
        EditText edtPass = findViewById(R.id.editText3);

        Intent intent = getIntent();
        edtName.setText(intent.getStringExtra("Username"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Chọn hoạt động cần hiển thị
        welcomeActivity();
        // loginActivity();
        // registerActivity();
        // infoActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == 101){
            EditText edtUsername = findViewById(R.id.editText);
            EditText edtPassword = findViewById(R.id.editText1);
            edtUsername.setText(data.getStringExtra("username"));
            edtPassword.setText(data.getStringExtra("password"));
        }

        if(requestCode == 102 && resultCode == 101){
            EditText edtUsername = findViewById(R.id.editText);
            EditText edtPassword = findViewById(R.id.editText1);
            edtUsername.setText(data.getStringExtra("username"));
            edtPassword.setText(data.getStringExtra("password"));
        }
    }
}