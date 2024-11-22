package com.example.fruitify_offcial.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;  // Don't forget to import Glide
import com.example.fruitify_offcial.databinding.ItemViewCartProductsBinding;
import com.example.fruitify_offcial.models.Product;
import com.example.fruitify_offcial.roomdb.CartProductDao;
import com.example.fruitify_offcial.roomdb.CartProducts;

public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.CartProductsViewHolder> {

    public final AsyncListDiffer<CartProducts> differ = new AsyncListDiffer<>(this, new DiffUtil.ItemCallback<CartProducts>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartProducts oldItem, @NonNull CartProducts newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull CartProducts oldItem, @NonNull CartProducts newItem) {
            return oldItem.equals(newItem);
        }
    });

    @NonNull
    @Override
    public CartProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewCartProductsBinding binding = ItemViewCartProductsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartProductsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductsViewHolder holder, int position) {
        CartProducts product = differ.getCurrentList().get(position);

        Glide.with(holder.itemView.getContext())
                .load(product.getProductImageUri())
                .into(holder.binding.ivProductImage);

        holder.binding.tvProductTitle.setText(product.getProductTitle());
        holder.binding.tvProductQuantity.setText(product.getProductQuantity());
        holder.binding.tvProductPrice.setText(product.getProductPrice());
        holder.binding.tvProductCount.setText(String.valueOf(product.getProductCount()));


    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public static class CartProductsViewHolder extends RecyclerView.ViewHolder {
        ItemViewCartProductsBinding binding;

        public CartProductsViewHolder(@NonNull ItemViewCartProductsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}

