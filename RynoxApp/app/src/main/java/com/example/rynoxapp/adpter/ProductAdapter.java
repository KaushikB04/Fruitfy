package com.example.rynoxapp.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.rynoxapp.Domain.ItemsDomain;
import com.example.rynoxapp.databinding.ViewholderPopListBinding;
import com.example.rynoxapp.Activity.detailActivity;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final ArrayList<ItemsDomain> items;
    private Context context;

    public ProductAdapter(ArrayList<ItemsDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Initialize context
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewholderPopListBinding binding = ViewholderPopListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set item data
        holder.binding.titleTxt.setText(items.get(position).getTitle());
        holder.binding.reviewTxt.setText(String.valueOf(items.get(position).getReview()));
        holder.binding.priceTxt.setText("$" + items.get(position).getPrice());
        holder.binding.ratingBar.setRating((float) items.get(position).getRating());
        holder.binding.ratingText.setText("(" + items.get(position).getRating() + ")");

        // Check if picUrl list is not null or empty
        if (items.get(position).getPicUrl() != null && !items.get(position).getPicUrl().isEmpty()) {
            // Load image using Glide
            RequestOptions requestOptions = new RequestOptions().transform(new CenterCrop());

            Glide.with(context)
                    .load(items.get(position).getPicUrl().get(0))
                    .apply(requestOptions)
                    .into(holder.binding.pic);
        }

        // Set item click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, detailActivity.class);
                intent.putExtra("object",items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderPopListBinding binding;

        public ViewHolder(ViewholderPopListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
