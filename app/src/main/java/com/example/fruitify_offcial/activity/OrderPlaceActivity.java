package com.example.fruitify_offcial.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.fruitify_offcial.Adapter.CartProductsAdapter;
import com.example.fruitify_offcial.CartListener;
import com.example.fruitify_offcial.R;
import com.example.fruitify_offcial.Utils;
import com.example.fruitify_offcial.databinding.ActivityOrderPlaceBinding;
import com.example.fruitify_offcial.databinding.AddressLayoutBinding;
import com.example.fruitify_offcial.databinding.BsPaymentBinding;
import com.example.fruitify_offcial.models.Order;
import com.example.fruitify_offcial.models.Product;
import com.example.fruitify_offcial.models.User;
import com.example.fruitify_offcial.roomdb.CartProducts;
import com.example.fruitify_offcial.viewModels.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import org.json.JSONObject;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrderPlaceActivity extends AppCompatActivity{
    private ActivityOrderPlaceBinding binding;
    private UserViewModel viewModel;
    private CartProductsAdapter cartProductsAdapter;
    private CartListener cartListener;
    List<CartProducts> cartProductsList;

    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_PAY_REQUEST_CODE) {
            if (data != null) {
                String response = data.getStringExtra("response");
                processUPIResponse(response);
            } else {
                Toast.makeText(this, "Transaction failed or canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderPlaceBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        cartProductsAdapter = new CartProductsAdapter();

        setContentView(binding.getRoot());
        getAllCartProducts();
        onPlaceOrderPlace();
        backToUserMainActivity();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void onPlaceOrderPlace() {
        binding.btnNextOrder.setOnClickListener(v -> {
            viewModel.getAddressStatus().observe(this, status -> {
                if (status) {
                    getPaymentView();
                } else {
                    AddressLayoutBinding addressLayoutBinding = AddressLayoutBinding.inflate(LayoutInflater.from(this));
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setView(addressLayoutBinding.getRoot())
                            .create();
                    alertDialog.show();

                    addressLayoutBinding.btnAdd.setOnClickListener(v1 -> {
                        saveAddress(alertDialog, addressLayoutBinding);
                    });
                }
            });
        });
    }

    private void getPaymentView() {
        BsPaymentBinding bsPaymentBinding = BsPaymentBinding.inflate(LayoutInflater.from(this));
        BottomSheetDialog bs = new BottomSheetDialog(this);
        bs.setContentView(bsPaymentBinding.getRoot());

        bsPaymentBinding.btnCashOnDelivery.setOnClickListener(v -> {
            processCashOnDelivery();
            bs.dismiss();
        });
        bsPaymentBinding.btnOnline.setOnClickListener(v -> {
            processOnlinePayment();
            bs.dismiss();
        });

        bs.show();
    }
    private void processCashOnDelivery() {
        saveOrder();
        viewModel.deleteCartProducts();
        viewModel.savingCartItemCount(0);
        if (cartListener != null) {
            cartListener.hide();
        } else {
            Log.e("OrderPlaceActivity", "cartListener is null, cannot call hide()");
        }
        Toast.makeText(this, "Your order is save", Toast.LENGTH_SHORT).show();

        Utils.hideDialog();
        startActivity(new Intent(OrderPlaceActivity.this, UsersMainActivity.class));
    }



    // Method to handle online payment
    private void processOnlinePayment() {
        paymentNow();
    }

    private void paymentNow() {
        String amg = binding.tvTotalTotal.getText().toString().replace("₹", "").trim();;
        if (amg.isEmpty() || !amg.matches("\\d+(\\.\\d+)?")) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", "kaushikbhanuse1234@okicici")
                .appendQueryParameter("pn", "studysparks")
                .appendQueryParameter("tn", "this your bill..")
                .appendQueryParameter("am", amg)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);

    }
    private void processUPIResponse(String response) {
        if (response == null || response.isEmpty()) {
            Toast.makeText(this, "Transaction failed or canceled", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse response to check transaction status
        boolean isSuccess = response.toLowerCase().contains("success");

        if (isSuccess) {
            // Perform actions after successful payment
            saveOrder();
            viewModel.deleteCartProducts();
            viewModel.savingCartItemCount(0);

            if (cartListener != null) {
                cartListener.hide();
            }

            Utils.hideDialog();

            // Navigate to UsersMainActivity
            startActivity(new Intent(OrderPlaceActivity.this, UsersMainActivity.class));
            Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Transaction failed or canceled", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveOrder() {
        viewModel.getAll().observe(this, cartProductsList1 -> {
            if(!cartProductsList1.isEmpty()) {


                viewModel.getUserAddress(address -> {
                    Order order = new Order();
                    order.setOrderId(getRandomId(10));
                    order.setOrderList(cartProductsList1);
                    order.setUserAddress(address);
                    order.setOrderStatus(0);
                    order.setOrderDate(Utils.getCurrentDate());
                    order.setOrderingUserId(Utils.getCurrentUserID());

                    viewModel.saveOrderedProducts(order);
                });
                for (CartProducts products : cartProductsList1) {
                    int count = products.getProductCount();
                    int stock = products.getProductStock() - count;
                    viewModel.saveProductsAfterOrder(stock, products);
                }
            }

        });
    }
    private String getRandomId(int length){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();

        return IntStream.range(0, length)
                .mapToObj(i -> String.valueOf(characters.charAt(random.nextInt(characters.length()))))
                .collect(Collectors.joining());
    }


    private void saveAddress(AlertDialog alertDialog, AddressLayoutBinding addressLayoutBinding) {
        Utils.showDialog(this, "Processing...");

        String userPinCode = addressLayoutBinding.etPinCode.getText().toString();
        String userArea = addressLayoutBinding.etArea.getText().toString();
        String userLandMarks = addressLayoutBinding.etLandmark.getText().toString();
        String userCity = addressLayoutBinding.etCity.getText().toString();
        String userFlat = addressLayoutBinding.etFlat.getText().toString();

        if (userPinCode.isEmpty() || userArea.isEmpty() || userLandMarks.isEmpty() || userCity.isEmpty() || userFlat.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            Utils.hideDialog();
            return;
        }

        String address = userLandMarks + ", " + userFlat + ", " + userArea + ", " + userCity + ", " + userPinCode;


        viewModel.saveUserAddress(address);
        viewModel.saveAddressStatus();

        Toast.makeText(this, "Address Saved", Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();


        getPaymentView();
    }

    private void backToUserMainActivity() {
        binding.tbOrderFragment.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this, UsersMainActivity.class));
            finish();
        });
    }

    private void getAllCartProducts() {
        viewModel.getAll().observe(this, cartProductList -> {
            binding.rvProductsItem.setAdapter(cartProductsAdapter);
            cartProductsAdapter.differ.submitList(cartProductList);

            double totalPrice = 0; // Changed type to double for precision
            for (CartProducts product : cartProductList) {
                try {
                    double price = Double.parseDouble(product.getProductPrice().substring(1));
                    // Ensure this returns a string without currency symbol
                    int productCount = product.getProductCount();
                    totalPrice += price * productCount;

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            binding.tvSubTotal.setText(String.format("%.2f", totalPrice));

            if (totalPrice < 200) {
                binding.tvDeliveryCharge.setText("Free");

            } else {
                binding.tvDeliveryCharge.setText("Free");
            }

            binding.tvTotalTotal.setText(String.format("₹%.2f", totalPrice));

        });
    }

}