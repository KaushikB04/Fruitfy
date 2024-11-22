package com.example.parkingfinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainNavigation extends AppCompatActivity {
    BottomNavigationView bn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        bn = findViewById(R.id.bnView);

        bn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.nav_home)
                {
                    Fragment fragment = new Fragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();

                }
                else if (id == R.id.nav_noti)
                {

                }
                else
                {

                }


                return true;
            }
        });
        bn.setSelectedItemId(R.id.nav_home);
    }
}