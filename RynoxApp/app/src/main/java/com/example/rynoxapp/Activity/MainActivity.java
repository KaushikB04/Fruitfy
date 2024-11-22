package com.example.rynoxapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import com.example.rynoxapp.Domain.CategoryDomain;
import com.example.rynoxapp.Domain.ItemsDomain;
import com.example.rynoxapp.Domain.SliderItems;
import com.example.rynoxapp.adpter.CategoryAdapter;
import com.example.rynoxapp.adpter.ProductAdapter;
import com.example.rynoxapp.adpter.SliderAdapter;
import com.example.rynoxapp.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initBanner();
        initCategory();
        initProduct();
        bottomNavigation();
    }

    private void bottomNavigation() {
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CartActivity.class));
            }
        });
    }

    private void initProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Items");
        binding.progressBarproduct.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        ItemsDomain item = issue.getValue(ItemsDomain.class);
                        if (item != null) {
                            items.add(item);
                        }
                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewProduct.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        binding.recyclerViewProduct.setAdapter(new ProductAdapter(items));
                    }
                }
                binding.progressBarproduct.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("initProduct", "Failed to read items", error.toException());
                binding.progressBarproduct.setVisibility(View.GONE);
            }
        });
    }

    private void initCategory() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarcategory.setVisibility(View.VISIBLE);
        ArrayList<CategoryDomain> items = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        CategoryDomain item = issue.getValue(CategoryDomain.class);
                        if (item != null) {
                            items.add(item);
                        }
                    }
                    if (!items.isEmpty()) {
                        binding.recyclerviewCatagory.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerviewCatagory.setAdapter(new CategoryAdapter(items));
                    }
                }
                binding.progressBarcategory.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("initCategory", "Failed to read categories", error.toException());
                binding.progressBarcategory.setVisibility(View.GONE);
            }
        });
    }

    private void initBanner() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        SliderItems item = issue.getValue(SliderItems.class);
                        if (item != null) {
                            items.add(item);
                        }
                    }
                    banners(items);
                }
                binding.progressBarBanner.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("initBanner", "Failed to read banners", error.toException());
                binding.progressBarBanner.setVisibility(View.GONE);
            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        SliderAdapter adapter = new SliderAdapter(items, binding.viewpageSlider);
        binding.viewpageSlider.setAdapter(adapter);
        binding.viewpageSlider.setClipToPadding(false);
        binding.viewpageSlider.setClipChildren(false);
        binding.viewpageSlider.setOffscreenPageLimit(3);
        binding.viewpageSlider.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewpageSlider.setPageTransformer(compositePageTransformer);
    }
}
