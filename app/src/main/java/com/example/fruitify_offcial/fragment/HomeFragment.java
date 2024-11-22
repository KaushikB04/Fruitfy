        package com.example.fruitify_offcial.fragment;

        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AlertDialog;
        import androidx.core.view.GravityCompat;
        import androidx.fragment.app.Fragment;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProvider;
        import androidx.navigation.NavController;
        import androidx.navigation.Navigation;
        import androidx.recyclerview.widget.GridLayoutManager;

        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.fruitify_offcial.Adapter.CartProductsAdapter;
        import com.example.fruitify_offcial.Adapter.CategoryAdapter;
        import com.example.fruitify_offcial.Adapter.ProductAdapter;
        import com.example.fruitify_offcial.CartListener;
        import com.example.fruitify_offcial.Constant;
        import com.example.fruitify_offcial.R;
        import com.example.fruitify_offcial.activity.MainActivity;
        import com.example.fruitify_offcial.databinding.ActivityMainBinding;
        import com.example.fruitify_offcial.databinding.AdressBookLayoutBinding;
        import com.example.fruitify_offcial.databinding.FragmentHomeBinding;
        import com.example.fruitify_offcial.databinding.ItemViewProductBinding;
        import com.example.fruitify_offcial.models.Category;
        import com.example.fruitify_offcial.models.Product;
        import com.example.fruitify_offcial.models.User;
        import com.example.fruitify_offcial.roomdb.CartProducts;
        import com.example.fruitify_offcial.viewModels.UserViewModel;

        import java.util.ArrayList;
        import java.util.List;

        public class HomeFragment extends Fragment {
            private FragmentHomeBinding binding;
            private ProductAdapter productAdapter;
            private UserViewModel viewModel;
            private CartListener cartListener = null;
            private CartProductsAdapter cartProductsAdapter;

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                binding = FragmentHomeBinding.inflate(inflater, container, false);


                viewModel = new ViewModelProvider(this).get(UserViewModel.class);
                productAdapter = new ProductAdapter();
                productAdapter = new ProductAdapter(this::onAddButtonClicked,this::onIncrementButtonClicked,this::onDecrementButtonClicked);
                binding.rvProductsHome.setLayoutManager(new GridLayoutManager(getContext(), 2));
                binding.rvProductsHome.setAdapter(productAdapter);
                cartProductsAdapter = new CartProductsAdapter();

                fetchProductsForHome();
                setNavigationListener();
                setAIICategories();
                onOrderClicked();
                navigatingToSearchFragment();
                return binding.getRoot();
            }

            private void onOrderClicked() {
                binding.cartHomeIcon.setOnClickListener(v -> {
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_homeFragment_to_orderFragment);
                });
            }

            private void onAddButtonClicked(Product product, ItemViewProductBinding productBinding){
                productBinding.tvAdd.setVisibility(View.GONE);
                productBinding.llProductCount.setVisibility(View.VISIBLE);

                int itemcount = Integer.parseInt(productBinding.tvProductCount.getText().toString());
                itemcount++;
                productBinding.tvProductCount.setText(String.valueOf(itemcount));

                cartListener.showCartLayout(1);

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
            private void fetchProductsForHome() {
                binding.shimmerViewContainerHome.setVisibility(View.VISIBLE);
                viewModel.fetchAllTheProducts().thenAccept(products -> {
                    if (products != null && !products.isEmpty()) {
                        productAdapter.setProducts(products);
                        binding.rvProductsHome.setVisibility(View.VISIBLE);
                        binding.tvTexts.setVisibility(View.GONE);
                    } else {
                        binding.rvProductsHome.setVisibility(View.GONE);
                        binding.tvTexts.setVisibility(View.VISIBLE);
                    }
                    binding.shimmerViewContainerHome.setVisibility(View.GONE);
                }).exceptionally(ex -> {
                    binding.shimmerViewContainerHome.setVisibility(View.GONE);
                    // Optionally show a message to the user
                    return null;
                });
            }
            private void setNavigationListener() {
                User user = new User();

                // Open navigation drawer on icon click
                binding.leftIcon.setOnClickListener(v -> {
                    binding.drawerLayout.openDrawer(GravityCompat.START);
                });

                // Access the header view from the navigation drawer
                View headerView = binding.leftNavigationView.getHeaderView(0);
                TextView userNumber = headerView.findViewById(R.id.tvUserNumber1);
                userNumber.setText(user.getUserPhoneNumber());

                headerView.findViewById(R.id.tvAddress).setOnClickListener(v -> {
                    AdressBookLayoutBinding adressBookLayoutBinding = AdressBookLayoutBinding.inflate(LayoutInflater.from(requireContext()));
                    viewModel.getUserAddress(address -> {
                        adressBookLayoutBinding.etAddress.setText(address.toString());
                    });
                    AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                            .setView(adressBookLayoutBinding.getRoot())
                            .create();
                    alertDialog.show();
                    adressBookLayoutBinding.btnEdit.setOnClickListener(v1 -> {
                        adressBookLayoutBinding.etAddress.setEnabled(true);
                    });
                    adressBookLayoutBinding.btnSave.setOnClickListener(v1 -> {
                        viewModel.saveAddress(adressBookLayoutBinding.etAddress.getText().toString());
                        alertDialog.dismiss();
                        Toast.makeText(requireContext(), "Address updated.", Toast.LENGTH_SHORT).show();
                    });
                });

                headerView.findViewById(R.id.tvLoginOut).setOnClickListener(v -> {
                    AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                            .setTitle("Log Out")
                            .setMessage("Do you want to log out?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    viewModel.logOutUser();

                                    // Navigate to the main activity
                                    Intent intent = new Intent(requireContext(), MainActivity.class);
                                    startActivity(intent);

                                    requireActivity().finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    alertDialog.show();
                });
            }


            // Set categories (Optional for HomeFragment)
            private void setAIICategories() {
                ArrayList<Category> categoryList = new ArrayList<>();
                for (int i = 0; i < Constant.allProductCatName.length; i++) {
                    categoryList.add(new Category(Constant.allProductCatName[i], Constant.allProductCatImg[i]));
                }
                binding.catView.setAdapter(new CategoryAdapter(categoryList,this::OnCategoryIconClicked));
            }

            private void OnCategoryIconClicked(Category category) {
                Bundle bundle = new Bundle();
                bundle.putString("category", category.getTitle());
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_homeFragment_to_categoryFragment, bundle);
            }



            // Navigate to SearchFragment when clicking on search bar
            private void navigatingToSearchFragment() {
                binding.etHomeSearch.setOnClickListener(v -> {
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_homeFragment_to_searchFragment);
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
