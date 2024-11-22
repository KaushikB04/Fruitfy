package com.example.fruitify.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.fruitify.R;

public class SignActivity extends BaseActivity {
    Button signupage_to_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign);
        signupage_to_login = findViewById(R.id.sign_btn);

        signupage_to_login.setOnClickListener(v -> {
            startActivity(new Intent(SignActivity.this, LoginActivity.class));
        });
    }
}