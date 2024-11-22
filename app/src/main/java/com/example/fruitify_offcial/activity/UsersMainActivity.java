package com.example.fruitify_offcial.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.fruitify_offcial.Adapter.CartProductsAdapter;
import com.example.fruitify_offcial.CartListener;
import com.example.fruitify_offcial.databinding.ActivityUsersMainBinding;
import com.example.fruitify_offcial.databinding.BsCartProductsBinding;
import com.example.fruitify_offcial.models.Product;
import com.example.fruitify_offcial.roomdb.CartProducts;
import com.example.fruitify_offcial.viewModels.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class UsersMainActivity extends AppCompatActivity implements CartListener {

    private ActivityUsersMainBinding binding;
    private UserViewModel viewModel;
    private CartProductsAdapter cartProductsAdapter;
    private List<CartProducts> cartProductsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        cartProductsAdapter = new CartProductsAdapter();

        initializeCartUI();

        setupWindowInsets();
    }



    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeCartUI() {
        getAllCartsProducts();
        getTotalCountInCart();
        onCartClicked();
        onNextButtonClicked();


    }

    private void onNextButtonClicked() {
        binding.btnNext.setOnClickListener( v -> {
            startActivity(new Intent(this, OrderPlaceActivity.class));
        });
    }

    private void getAllCartsProducts() {
        viewModel.getProductsLiveData().observe(this, new Observer<List<CartProducts>>() {
            @Override
            public void onChanged(List<CartProducts> cartProducts) {
                if (cartProducts != null && !cartProducts.isEmpty()) {
                    cartProductsList = cartProducts;
                    cartProductsAdapter.differ.submitList(cartProducts);
                }
            }
        });

    }
    private void getTotalCountInCart() {
        viewModel.fetchTotalCartItemCount().observe(this, integer -> {
            if (integer > 0 && binding.llCart != null) {
                binding.llCart.setVisibility(View.VISIBLE);
                binding.tvNumberOfProductCount.setText(String.valueOf(integer));
            } else if (binding.llCart != null) {
                binding.llCart.setVisibility(View.GONE);
                binding.tvNumberOfProductCount.setText("0");
            }
        });
    }



    @Override
    public void showCartLayout(int itemCount) {
        int previousCount = Integer.parseInt(binding.tvNumberOfProductCount.getText().toString());
        int updateCount = previousCount + itemCount;

        if (updateCount>0){
            binding.llCart.setVisibility(View.VISIBLE);
            binding.tvNumberOfProductCount.setText(String.valueOf(updateCount));
        }
        else {
            binding.llCart.setVisibility(View.GONE);
            binding.tvNumberOfProductCount.setText("0");
        }
    }

    @Override
    public void savingCartItemCount(int itemCount) {
        viewModel.fetchTotalCartItemCount().observe(this ,integer -> {
            viewModel.savingCartItemCount(integer + itemCount);
        });

    }

    @Override
    public void onCartClicked() {
        binding.llitemCart.setOnClickListener(v -> {
            BsCartProductsBinding bsCartProductsBinding = BsCartProductsBinding.inflate(LayoutInflater.from(this));
            BottomSheetDialog bs = new BottomSheetDialog(this);
            bs.setContentView(bsCartProductsBinding.getRoot());

            // Set the cart items and count
            bsCartProductsBinding.tvNumberOfProductCount.setText(binding.tvNumberOfProductCount.getText());
            bsCartProductsBinding.rvProductsItem.setAdapter(cartProductsAdapter);

            // Submit the latest list to the adapter before displaying
            cartProductsAdapter.differ.submitList(cartProductsList);

            bsCartProductsBinding.btnNext.setOnClickListener(v1 -> {
                startActivity(new Intent(this, OrderPlaceActivity.class));
                bs.dismiss(); // Close the bottom sheet when navigating
            });

            // Show the bottom sheet
            bs.show();
        });
    }

    @Override
    public void hide() {
        if (binding != null && binding.llCart != null) {
            binding.llCart.setVisibility(View.GONE);
        }
        if (binding != null && binding.tvNumberOfProductCount != null) {
            binding.tvNumberOfProductCount.setText("0");
        }
    }


}
