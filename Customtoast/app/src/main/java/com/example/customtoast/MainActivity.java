package com.example.customtoast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
 Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater Inflater = getLayoutInflater();
                View view = Inflater.inflate(R.layout.tosat,(ViewGroup)findViewById(R.id.Tos));
                Toast t = new Toast(getApplicationContext());
                t.setView(view);
                t.setDuration(Toast.LENGTH_SHORT);
                t.show();
            }
        });
    }
}