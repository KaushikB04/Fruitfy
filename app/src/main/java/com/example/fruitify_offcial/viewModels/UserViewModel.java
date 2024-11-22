    package com.example.fruitify_offcial.viewModels;

    import android.app.Application;
    import android.content.Context;
    import android.content.SharedPreferences;

    import androidx.annotation.NonNull;
    import androidx.lifecycle.AndroidViewModel;
    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.MutableLiveData;

    import com.example.fruitify_offcial.Utils;
    import com.example.fruitify_offcial.models.Order;
    import com.example.fruitify_offcial.models.Product;
    import com.example.fruitify_offcial.models.User;
    import com.example.fruitify_offcial.roomdb.CartProductDao;
    import com.example.fruitify_offcial.roomdb.CartProductDataBase;
    import com.example.fruitify_offcial.roomdb.CartProducts;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.Query;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.StringJoiner;
    import java.util.concurrent.CompletableFuture;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class UserViewModel extends AndroidViewModel {
        // initialization
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("MY_pref",Context.MODE_PRIVATE);
        CartProductDao cartProductDao = CartProductDataBase.getDatabaseInstance(getApplication()).cartProductsDao();

        // RoomDB

        public void insertCartProduct(CartProducts products){
            cartProductDao.insertCartProduct(products);
        }
        public void updateCartProduct(CartProducts products){
            cartProductDao.updateCartProduct(products);
        }
        public  void deleteCartProduct(String productId){
            cartProductDao.deleteCartProduct(productId);
        }
        public void deleteCartProducts(){
            cartProductDao.deleteCartProducts();
        }

        public LiveData<List<CartProducts>> getAll(){
            return cartProductDao.getAllProducts();
        }


        public UserViewModel(@NonNull Application application) {
            super(application);
        }

        //firebase call


        public void updateItemCount(Product product, int itemCount) {
            FirebaseDatabase.getInstance().getReference("Admins").child("AllProduct").child(product.getProductRandomId()).child("itemCount").setValue(itemCount);
        }


        public LiveData<List<CartProducts>> getProductsLiveData() {
            return cartProductDao.getAllProducts();
        }


        //shared preference
        public void savingCartItemCount(int itemCount){

            sharedPreferences.edit().putInt("itemCount",itemCount).apply();
        }

        public MutableLiveData<Integer> fetchTotalCartItemCount(){

            MutableLiveData<Integer> totalItemCount= new MutableLiveData<Integer>();
            totalItemCount.setValue(sharedPreferences.getInt("itemCount",0));
            return totalItemCount;
        }
        public MutableLiveData<Boolean> getAddressStatus(){
            MutableLiveData<Boolean> status = new MutableLiveData<>();
            status.setValue(sharedPreferences.getBoolean("addressStatus",false));
            return status;
        }
        public void saveUserAddress(String address){
            FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").child(Utils.getCurrentUserID()).child("userAddress").setValue(address);
        }
        public void saveAddressStatus(){
            sharedPreferences.edit().putBoolean("addressStatus",true).apply();
        }
        public void saveProductsAfterOrder(int stock,CartProducts products){
            FirebaseDatabase.getInstance().getReference("Admins").child("AllProduct").child(products.getProductId()).child("itemCount").setValue(0);
            FirebaseDatabase.getInstance().getReference("Admins").child("AllProduct").child(products.getProductId()).child("productStock").setValue(stock);
        }
        public void getOrderedProducts(String orderId,OrderedProductsCallback callback){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Admins").child("Orders").child(orderId);
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null){
                        List<CartProducts> orderList = order.getOrderList();
                        callback.onOrderedProductsRetrieved(orderList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

        private void trySend(List<CartProducts> orderList) {
        }

        public void getUserAddress(final AddressCallback callback){
          DatabaseReference db =   FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").child(Utils.getCurrentUserID()).child("userAddress");
          db.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if (snapshot.exists()) {

                      String userAddress = snapshot.getValue(String.class);
                      callback.onAddressRetrieved(userAddress);
                  } else {
                      callback.onAddressRetrieved(null);
                  }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {
                callback.onAddressRetrieved(null);
              }
          });
        }
        public interface AddressCallback {
            void onAddressRetrieved(String address);
        }
        public void saveOrderedProducts(Order order){
            FirebaseDatabase.getInstance().getReference("Admins").child("Orders").child(order.getOrderId()).setValue(order);
        }

        public CompletableFuture<List<Order>> getAllOrders() {
            CompletableFuture<List<Order>> future = new CompletableFuture<>();


            Query dbQuery = FirebaseDatabase.getInstance().getReference("Admins")
                    .child("Orders")
                    .orderByChild("orderStatus");

            dbQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Order> orderList = new ArrayList<>();
                    for (DataSnapshot orders : snapshot.getChildren()) {
                        Order order = orders.getValue(Order.class);
                        if (order != null && order.getOrderingUserId().equals(Utils.getCurrentUserID())) {
                            orderList.add(order);
                        }
                    }
                    future.complete(orderList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    future.completeExceptionally(new Exception("DatabaseError: " + error.getMessage()));
                }
            });

            return future;
        }


        public CompletableFuture<List<Product>> fetchAllTheProducts() {
            CompletableFuture<List<Product>> future = new CompletableFuture<>();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admins").child("AllProduct");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    ArrayList<Product> products = new ArrayList<>();
                    if (snapshot.exists()) { // Check if snapshot is not empty
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            Product product = productSnapshot.getValue(Product.class);
                            if (product != null) {
                                products.add(product);
                            }
                        }
                    }
                    future.complete(products);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    future.completeExceptionally(error.toException());
                }
            });

            return future;
        }


        public CompletableFuture<List<Product>> getCategoryProduct(String category) {
            CompletableFuture<List<Product>> future = new CompletableFuture<>();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Admins").child("AllProduct");

            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Product> products = new ArrayList<>();
                    if (snapshot.exists()) {
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            Product product = productSnapshot.getValue(Product.class);
                            if (product != null && ("All".equals(category) || product.getProductCategory().equals(category))) {
                                products.add(product);
                            }
                        }
                    }
                    future.complete(products);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    future.completeExceptionally(error.toException());
                }
            });
            return future;
        }
        public interface OrderedProductsCallback {
            void onOrderedProductsRetrieved(List<CartProducts> orderList);
        }
        public void saveAddress(String address){
            FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").child(Utils.getCurrentUserID()).child("userAddress").setValue(address);
        }

        public void logOutUser(){
            FirebaseAuth.getInstance().signOut();
        }
    }

