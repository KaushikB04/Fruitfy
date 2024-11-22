package com.example.parkingfinder2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class mapFragment extends AppCompatActivity  {
 GoogleMap gmap;

  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        gmap = googleMap;
                        LatLng punlatLng = new LatLng(18.5204,73.8567);
                        MarkerOptions markerOptions = new MarkerOptions().position(punlatLng).title("Pune");
                        gmap.addMarker(markerOptions);
                        gmap.moveCamera(CameraUpdateFactory.newLatLng(punlatLng));
                        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(punlatLng,16f));
                    }
                });

            }
        });
        return view;


    }
}