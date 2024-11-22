package com.example.fruitify.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fruitify.databinding.ActivityIntroBinding;

public class introActivity extends BaseActivity {
    ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(introActivity.this, MainActivity.class));
            }
        });
        binding.loginNxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(introActivity.this, LoginActivity.class));
            }
        });

    }

    }
