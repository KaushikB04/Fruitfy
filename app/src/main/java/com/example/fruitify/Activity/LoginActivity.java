package com.example.fruitify.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fruitify.R;

public class LoginActivity extends BaseActivity {
    TextView sign_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign_up = findViewById(R.id.signupbtn);

        sign_up.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,SignActivity.class));
        });
    }
}