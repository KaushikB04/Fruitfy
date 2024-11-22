package com.example.fruitify_offcial.fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fruitify_offcial.Adapter.OrdersAdapter;
import com.example.fruitify_offcial.R;
import com.example.fruitify_offcial.databinding.FragmentOrderBinding;
import com.example.fruitify_offcial.models.Order;
import com.example.fruitify_offcial.models.OrderedItems;
import com.example.fruitify_offcial.roomdb.CartProducts;
import com.example.fruitify_offcial.viewModels.AuthViewModel;
import com.example.fruitify_offcial.viewModels.UserViewModel;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    private FragmentOrderBinding binding;
    private UserViewModel viewModel;
    private OrdersAdapter ordersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        ordersAdapter = new OrdersAdapter(requireContext(), this ::onOrderItemViewClicked);
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvOrders.setAdapter(ordersAdapter);

        backButton();
        getAllOrders();

        return binding.getRoot();
    }

    // Fetch all orders and process them
    private void getAllOrders() {

        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        viewModel.getAllOrders().thenAccept(ordersList -> {
            if (!ordersList.isEmpty()) {
                ArrayList<OrderedItems> orderedList = new ArrayList<>();
                for (Order order : ordersList) {
                    StringBuilder title = new StringBuilder();
                    double totalPrice = 0;
                    for (CartProducts product : order.getOrderList()) {
                        try {
                            double price = Double.parseDouble(product.getProductPrice().substring(1));
                            int productCount = product.getProductCount();
                            totalPrice += price * productCount;

                            title.append(product.getProductTitle()).append(", ");
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    StringBuilder finalTitle = new StringBuilder(title.toString().replaceAll(", $", ""));

                    OrderedItems orderedItems = new OrderedItems(order.getOrderId(), order.getOrderDate(), order.getOrderStatus(), finalTitle, totalPrice);
                    orderedList.add(orderedItems);
                }

                ordersAdapter.differ.submitList(orderedList);
                binding.shimmerViewContainer.setVisibility(View.GONE);
            }
        });
    }

    public void onOrderItemViewClicked (OrderedItems orderedItems){
        Bundle bundle = new Bundle();
        bundle.putInt("status", orderedItems.getItemStatus());
        bundle.putString("orderId",orderedItems.getOrderId());

        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_orderFragment_to_detailFragment, bundle);
    }

    // Back button navigation handling
    private void backButton() {
        binding.tbOrderFragment.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_orderFragment_to_homeFragment);
        });
    }
}
