package com.example.fruitify_offcial.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fruitify_offcial.Adapter.ProductAdapter;
import com.example.fruitify_offcial.CartListener;
import com.example.fruitify_offcial.R;
import com.example.fruitify_offcial.databinding.FragmentSearchBinding;
import com.example.fruitify_offcial.databinding.ItemViewProductBinding;
import com.example.fruitify_offcial.models.Product;
import com.example.fruitify_offcial.roomdb.CartProducts;
import com.example.fruitify_offcial.viewModels.UserViewModel;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private ProductAdapter productAdapter;
    private UserViewModel viewModel;
    private CartListener cartListener = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);


        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        productAdapter = new ProductAdapter();

        productAdapter = new ProductAdapter(this::onAddButtonClicked,this::onIncrementButtonClicked,this::onDecrementButtonClicked);
        binding.rvProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvProduct.setAdapter(productAdapter);


        getAllProducts();
        backToHomeFragment();
        searchProducts();

        return binding.getRoot();
    }
    private void onAddButtonClicked(Product product, ItemViewProductBinding productBinding){
        productBinding.tvAdd.setVisibility(View.GONE);
        productBinding.llProductCount.setVisibility(View.VISIBLE);

        //step 1
        int itemcount = Integer.parseInt(productBinding.tvProductCount.getText().toString());
        itemcount++;
        productBinding.tvProductCount.setText(String.valueOf(itemcount));

        cartListener.showCartLayout(1);

        //step 2
        product.setItemCount((int) itemcount);
        cartListener.savingCartItemCount(1);
        saveProductInRoomDb(product);
        viewModel.updateItemCount(product,itemcount);

    }

    private void onIncrementButtonClicked(Product product,ItemViewProductBinding productBinding){
        int itemcountInc = Integer.parseInt(productBinding.tvProductCount.getText().toString());
        itemcountInc++;

        if (product.getProductStock() + 1 > itemcountInc ){

            productBinding.tvProductCount.setText(String.valueOf(itemcountInc));

            cartListener.showCartLayout(1);

            //step 2
            product.setItemCount((int) itemcountInc);
            cartListener.savingCartItemCount(1);
            saveProductInRoomDb(product);
            viewModel.updateItemCount(product,itemcountInc);
        }
        else {
            Toast.makeText(requireContext(), "Cant Add more of this", Toast.LENGTH_SHORT).show();
        }
    }
    private void  onDecrementButtonClicked(Product product,ItemViewProductBinding productBinding){
        int itemcountDec = Integer.parseInt(productBinding.tvProductCount.getText().toString());
        itemcountDec--;

        product.setItemCount((int) itemcountDec);
        cartListener.savingCartItemCount(-1);
        saveProductInRoomDb(product);
        viewModel.updateItemCount(product,itemcountDec);

        if (itemcountDec > 0){
            productBinding.tvProductCount.setText(String.valueOf(itemcountDec));
        }
        else {
            viewModel.deleteCartProduct(product.getProductRandomId());
            productBinding.tvAdd.setVisibility(View.VISIBLE);
            productBinding.llProductCount.setVisibility(View.GONE);
            productBinding.tvProductCount.setText("0");
        }


        cartListener.showCartLayout(-1);

        // step2


    }

    private void saveProductInRoomDb(Product product) {
        CartProducts cartProduct = new CartProducts(
                product.getProductRandomId(),
                product.getProductTitle(),
                product.getProductQuantity() + product.getProductUnit(),
                "₹" + product.getProductPrice(),
                Math.toIntExact(product.getItemCount()),
                Math.toIntExact(product.getProductStock()),
                product.getProductImageUris(),
                product.getProductCategory(),
                product.getAdminUid()
        );
        viewModel.insertCartProduct(cartProduct);
    }

    private void backToHomeFragment() {
        binding.backSearchBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_searchFragment_to_homeFragment);
        });
    }
    private void searchProducts() {
        binding.searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                productAdapter.getFilter().filter(query);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getAllProducts() {
        binding.shimmerViewCaontainer.setVisibility(View.VISIBLE);
        viewModel.fetchAllTheProducts().thenAccept(products -> {
            if (products != null && !products.isEmpty()) {
                productAdapter.setProducts(products);
                binding.rvProduct.setVisibility(View.VISIBLE);
                binding.tvText.setVisibility(View.GONE); // Hide "No products" text
            } else {
                binding.rvProduct.setVisibility(View.GONE);
                binding.tvText.setVisibility(View.VISIBLE); // Show "No products" text
            }
            binding.shimmerViewCaontainer.setVisibility(View.GONE); // Hide loading
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CartListener) {
            cartListener = (CartListener) context;
        } else {
            throw new ClassCastException("Please implement CartListener");
        }
    }
}
