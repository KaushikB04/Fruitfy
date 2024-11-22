package com.example.fruitify.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fruitify.Adapter.productAdapter;
import com.example.fruitify.Domain.ItemDomain;
import com.example.fruitify.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initProductList();

    }

    private void initProductList() {
        DatabaseReference myRef = database.getReference("Items");
        binding.progressBarDeal.setVisibility(View.VISIBLE);
        ArrayList<ItemDomain> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue : snapshot.getChildren()){
                        list.add(issue.getValue(ItemDomain.class));
                    }
                    if(!list.isEmpty()){
                        binding.productView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        binding.productView.setAdapter(new productAdapter(list));
                    }
                    binding.progressBarDeal.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}