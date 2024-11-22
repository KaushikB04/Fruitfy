package com.example.parkingfinder2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class login extends AppCompatActivity {
    TextView fp;
    Button next;
    ImageButton google, facebook,twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fp = findViewById(R.id.fp);
        next = findViewById(R.id.next);
        google = findViewById(R.id.google);
        facebook = findViewById(R.id.fb);
        twitter = findViewById(R.id.twitter);


        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(login.this, MainNavigation.class);
                startActivity(intent);
            }
        });


    }
}