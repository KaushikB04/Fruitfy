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

import com.example.fruitify_offcial.Adapter.CartProductsAdapter;
import com.example.fruitify_offcial.R;
import com.example.fruitify_offcial.databinding.FragmentDetailBinding;
import com.example.fruitify_offcial.roomdb.CartProducts;
import com.example.fruitify_offcial.viewModels.UserViewModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetailFragment extends Fragment {

    FragmentDetailBinding binding;
    private UserViewModel viewModel;
    int status = 0;
    String orderId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding = FragmentDetailBinding.inflate(inflater,container,false);
        getValue();
        settingStatus();
        getOrderedProducts();
        backButton();
        return binding.getRoot();
    }
    private void getOrderedProducts() {
        viewModel.getOrderedProducts(orderId, new UserViewModel.OrderedProductsCallback() {
            @Override
            public void onOrderedProductsRetrieved(List<CartProducts> orderList) {
                CartProductsAdapter adapterCartProducts = new CartProductsAdapter ();
                binding.rvProductsItems.setAdapter(adapterCartProducts);
                adapterCartProducts.differ.submitList(orderList);
            }
        });
    }

    private void settingStatus() {
        Map<Integer, List<View>> statusToViews = new HashMap<>();
        statusToViews.put(0, Arrays.asList(binding.iv1));
        statusToViews.put(1, Arrays.asList(binding.iv1, binding.iv2, binding.view1));
        statusToViews.put(2, Arrays.asList(binding.iv1, binding.iv2, binding.view1, binding.iv3, binding.view2));
        statusToViews.put(3, Arrays.asList(binding.iv1, binding.iv2, binding.view1, binding.iv3,binding.view2,binding.iv4, binding.view3));

        List<View> viewsToTint = statusToViews.getOrDefault(status, Collections.emptyList());

        for (View view : viewsToTint) {
            view.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue));
        }
    }

    private void backButton() {
        binding.tbDetails.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_detailFragment_to_orderFragment);
        });
    }
    private void getValue() {
        Bundle bundle = getArguments();
        status = bundle.getInt("status");
        orderId = bundle.getString("orderId");
    }
}