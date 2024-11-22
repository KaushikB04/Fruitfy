package com.example.fruitify_offcial.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitify_offcial.databinding.ViewHoldCategoryBinding;
import com.example.fruitify_offcial.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private ArrayList<Category> items;
    private Context context;
    private OnCategoryIconClicked onCategoryIconClicked;

    // Constructor for initializing items and click listener
    public CategoryAdapter(ArrayList<Category> items, OnCategoryIconClicked onCategoryIconClicked) {
        this.items = items;
        this.onCategoryIconClicked = onCategoryIconClicked;
    }

    // Interface to handle category click events
    public interface OnCategoryIconClicked {
        void onCategoryClicked(Category category);
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHoldCategoryBinding binding = ViewHoldCategoryBinding.inflate(inflater, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category category = items.get(position);
        ViewHoldCategoryBinding binding = holder.binding;

        // Set category image and title
        binding.imgCatName.setImageResource(category.getImage());
        binding.titleCatName.setText(category.getTitle());

        // Set click listener for category item
        holder.itemView.setOnClickListener(v -> {
            if (onCategoryIconClicked != null) {
                onCategoryIconClicked.onCategoryClicked(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;  // Safely return size, if items is null
    }

    // ViewHolder class for holding category views
    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ViewHoldCategoryBinding binding;

        public CategoryViewHolder(@NonNull ViewHoldCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
