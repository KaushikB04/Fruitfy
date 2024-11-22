package com.example.upi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText amt;
    Button btn;
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123  && resultCode == RESULT_OK && data.getData()!= null){
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        amt= findViewById(R.id.amount);
        btn = findViewById(R.id.paybtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String am = amt.getText().toString();
                if (am.isEmpty()){
                    Toast.makeText(MainActivity.this, "please enter amount", Toast.LENGTH_SHORT).show();
                }
                else {
                    Uri uri =
                            new Uri.Builder()
                                    .scheme("upi")
                                    .authority("pay")
                                    .appendQueryParameter("pa", "kaushikbhanuse1234@okicici")
                                    .appendQueryParameter("pn", "studysparks")
                                    .appendQueryParameter("mc", "BCR2DN4TXWC7HPL2")
                                    .appendQueryParameter("tr", "23677")
                                    .appendQueryParameter("tn", "upi payment example")
                                    .appendQueryParameter("am", "am")
                                    .appendQueryParameter("cu", "INR")
                                    .appendQueryParameter("url", "your-transaction-url")
                                    .build();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
                    startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);

                }
            }
        });
    }
}