package com.example.fruitify_offcial.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fruitify_offcial.FilteringProducts;
import com.example.fruitify_offcial.databinding.ItemViewProductBinding;
import com.example.fruitify_offcial.models.Product;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {

    public final AsyncListDiffer<Product> differ = new AsyncListDiffer<>(this, new ProductDiffCallback());

    private final ArrayList<Product> originalList = new ArrayList<>();
    private FilteringProducts filter;
    private  OnButtonClickListener onButtonClickListener;
    private  OnIncrementButtonClickListener onIncrementButtonClickListener;
    private  OnDecrementButtonClickListener onDecrementButtonClickListener;
    public ProductAdapter(){

    }

    public ProductAdapter(OnButtonClickListener onButtonClickListener,
                          OnIncrementButtonClickListener onIncrementButtonClickListener,
                          OnDecrementButtonClickListener onDecrementButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
        this.onIncrementButtonClickListener = onIncrementButtonClickListener;
        this.onDecrementButtonClickListener = onDecrementButtonClickListener;
    }



    public void setProducts(List<Product> products) {
        originalList.clear();
        for (Product product : products) {
            product.setItemCount(0); // Reset item count
        }
        originalList.addAll(products);
        submitList(products);
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewProductBinding binding = ItemViewProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = differ.getCurrentList().get(position);

        // Set product details
        holder.binding.tvProductName.setText(product.getProductTitle());
        holder.binding.tvProductQuantity.setText(product.getProductQuantity() + " / " + product.getProductUnit());
        holder.binding.tvProductPrice.setText("₹" + product.getProductPrice());

        // Load product image
        if (product.getProductImageUris() != null && !product.getProductImageUris().isEmpty()) {
            Glide.with(holder.binding.isImageSlider.getContext())
                    .load(product.getProductImageUris().split(",")[0])
                    .into(holder.binding.isImageSlider);
        }

        // Update UI based on itemCount
        if (product.getItemCount() > 0) {
            // Show the count layout and hide the "Add" button
            holder.binding.tvProductCount.setText(String.valueOf(product.getItemCount()));
            holder.binding.tvAdd.setVisibility(View.GONE);
            holder.binding.llProductCount.setVisibility(View.VISIBLE);
        } else {
            // Hide the count layout and show the "Add" button
            holder.binding.tvProductCount.setText("0");
            holder.binding.tvAdd.setVisibility(View.VISIBLE);
            holder.binding.llProductCount.setVisibility(View.GONE);
        }

        // Add button click listener
        holder.binding.tvAdd.setOnClickListener(v -> {
            if (onButtonClickListener != null) {
                onButtonClickListener.onAddButtonClicked(product, holder.binding);
            }
        });

        // Increment button click listener
        holder.binding.tvIncrementCount.setOnClickListener(v -> {
            if (onIncrementButtonClickListener != null) {
                onIncrementButtonClickListener.onIncrementButtonClicked(product, holder.binding);
            }
        });

        // Decrement button click listener
        holder.binding.tvdecrementCount.setOnClickListener(v -> {
            if (onDecrementButtonClickListener != null) {
                onDecrementButtonClickListener.onDecrementButtonClicked(product, holder.binding);
            }
        });
    }


    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public void submitList(List<Product> products) {
        differ.submitList(products);
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilteringProducts(this, originalList);
        }
        return filter;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemViewProductBinding binding;

        public ProductViewHolder(@NonNull ItemViewProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class ProductDiffCallback extends DiffUtil.ItemCallback<Product> {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getProductRandomId().equals(newItem.getProductRandomId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.equals(newItem);
        }
    }

    public interface OnButtonClickListener {
        void onAddButtonClicked(Product product, ItemViewProductBinding binding);
    }
    public interface OnIncrementButtonClickListener {
        void onIncrementButtonClicked(Product product, ItemViewProductBinding binding);
    }
    public interface OnDecrementButtonClickListener {
        void onDecrementButtonClicked(Product product, ItemViewProductBinding binding);


    }
}
