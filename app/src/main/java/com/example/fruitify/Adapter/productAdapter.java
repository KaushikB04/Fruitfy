package com.example.fruitify.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fruitify.Domain.ItemDomain;
import com.example.fruitify.databinding.ViewHolderProductBinding;

import java.util.ArrayList;

public class productAdapter extends RecyclerView.Adapter<productAdapter.ProductViewHolder> {
    ArrayList<ItemDomain> items;
    Context context;

    public productAdapter(ArrayList<ItemDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public productAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            ViewHolderProductBinding binding = ViewHolderProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull productAdapter.ProductViewHolder holder, int position) {
        holder.binding.productTitle.setText(items.get(position).getTitle());
        holder.binding.productPrice.setText(items.get(position).getPrice() + "/rs");


        Glide.with(context)
                .load(items.get(position).getPicUrl())
                .into(holder.binding.productImage);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        ViewHolderProductBinding binding;
        public ProductViewHolder(ViewHolderProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

