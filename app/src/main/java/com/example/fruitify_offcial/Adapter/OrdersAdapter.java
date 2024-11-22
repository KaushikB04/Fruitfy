package com.example.fruitify_offcial.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitify_offcial.R;
import com.example.fruitify_offcial.databinding.ItemViewOrdersBinding;
import com.example.fruitify_offcial.models.OrderedItems;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private final Context context;
    private final OnOrderClickListener onOrderClickListener;

    private final DiffUtil.ItemCallback<OrderedItems> diffUtil = new DiffUtil.ItemCallback<OrderedItems>() {
        @Override
        public boolean areItemsTheSame(@NonNull OrderedItems oldItem, @NonNull OrderedItems newItem) {
            return oldItem.getOrderId().equals(newItem.getOrderId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull OrderedItems oldItem, @NonNull OrderedItems newItem) {
            return oldItem.equals(newItem);
        }
    };

    public final AsyncListDiffer<OrderedItems> differ = new AsyncListDiffer<>(this, diffUtil);

    public OrdersAdapter(Context context, OnOrderClickListener orderClickListener) {
        this.context = context;
        this.onOrderClickListener = orderClickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout using ViewBinding
        ItemViewOrdersBinding binding = ItemViewOrdersBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderedItems order = differ.getCurrentList().get(position);

        holder.binding.tvorderTitles.setText(order.getItemTitle());
        holder.binding.tvOrderDate.setText(order.getItemDate());
        holder.binding.tvOrderAmount.setText(String.format("₹%s", order.getItemPrice()));

        // Set order status based on status integer value
        Integer status = order.getItemStatus();
        if (status != null) {
            switch (status) {
                case 0:
                    holder.binding.tvOrderStatus.setText("Ordered");
                    holder.binding.tvOrderStatus.setBackgroundTintList(
                            ContextCompat.getColorStateList(context, R.color.yellow));
                    break;
                case 1:
                    holder.binding.tvOrderStatus.setText("Received");
                    holder.binding.tvOrderStatus.setBackgroundTintList(
                            ContextCompat.getColorStateList(context, R.color.blue));
                    break;
                case 2:
                    holder.binding.tvOrderStatus.setText("Dispatched");
                    holder.binding.tvOrderStatus.setBackgroundTintList(
                            ContextCompat.getColorStateList(context, R.color.green));
                    break;
                case 3:
                    holder.binding.tvOrderStatus.setText("Delivered");
                    holder.binding.tvOrderStatus.setBackgroundTintList(
                            ContextCompat.getColorStateList(context, R.color.orange));
                    break;
                default:
                    holder.binding.tvOrderStatus.setText("Unknown");
                    holder.binding.tvOrderStatus.setBackgroundTintList(
                            ContextCompat.getColorStateList(context, R.color.gray));
                    break;
            }
        } else {
            holder.binding.tvOrderStatus.setText("Unknown");
            holder.binding.tvOrderStatus.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, R.color.gray));
        }

        // Set onClickListener to handle clicks
        holder.itemView.setOnClickListener(v -> onOrderClickListener.onOrderItemViewClicked(order));
    }

    @Override
    public int getItemCount() {
        // Return the current size of the list from differ
        return differ.getCurrentList().size();
    }

    // ViewHolder class to hold and bind the view components
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ItemViewOrdersBinding binding;

        public OrderViewHolder(@NonNull ItemViewOrdersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnOrderClickListener {
        void onOrderItemViewClicked(OrderedItems orderedItems);
    }
}
