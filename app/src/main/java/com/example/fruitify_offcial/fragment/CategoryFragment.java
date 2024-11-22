package com.example.fruitify_offcial.fragment;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fruitify_offcial.Adapter.ProductAdapter;
import com.example.fruitify_offcial.CartListener;
import com.example.fruitify_offcial.R;
import com.example.fruitify_offcial.databinding.FragmentCategoryBinding;
import com.example.fruitify_offcial.databinding.ItemViewProductBinding;
import com.example.fruitify_offcial.models.Product;
import com.example.fruitify_offcial.roomdb.CartProducts;
import com.example.fruitify_offcial.viewModels.UserViewModel;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    private UserViewModel viewModel;
    private FragmentCategoryBinding binding;
    private String category = null;
    private ProductAdapter productAdapter;
    private CartListener cartListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        productAdapter = new ProductAdapter(this::onAddButtonClicked, this::onIncrementButtonClicked, this::onDecrementButtonClicked);
        binding.rvProductCategory.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvProductCategory.setAdapter(productAdapter);

        getProductCategory();
        fetchCategoryProduct();
        onSearchMenuClick();
        setToolBarTitle();
        onNavigationItemClick();


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

    private void fetchCategoryProduct() {
        binding.shimmerViewContainerCategory.setVisibility(View.VISIBLE);
        viewModel.getCategoryProduct(category).thenAccept(products -> {
            binding.shimmerViewContainerCategory.setVisibility(View.GONE);
            if (products != null && !products.isEmpty()) {
                productAdapter.setProducts(products);
                binding.rvProductCategory.setVisibility(View.VISIBLE);
                binding.tvTexts.setVisibility(View.GONE);
            } else {
                binding.rvProductCategory.setVisibility(View.GONE);
                binding.tvTexts.setVisibility(View.VISIBLE);
            }
        }).exceptionally(ex -> {
            binding.shimmerViewContainerCategory.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Error fetching products: " , Toast.LENGTH_SHORT).show();
            return null;
        });
    }

    private void onNavigationItemClick() {
        binding.tbSearchFragment.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_categoryFragment_to_homeFragment);
        });
    }

    private void onSearchMenuClick() {
        binding.tbSearchFragment.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.searchmenu) {
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_categoryFragment_to_searchFragment);
                return true;
            }
            return false;
        });
    }

    private void setToolBarTitle() {
        binding.tbSearchFragment.setTitle(category);
    }

    private void getProductCategory() {
        if (getArguments() != null) {
            category = getArguments().getString("category");
        }
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