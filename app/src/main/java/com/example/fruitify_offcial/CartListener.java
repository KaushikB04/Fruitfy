package com.example.fruitify_offcial;

public interface CartListener {
        void showCartLayout(int itemCount);
        void savingCartItemCount(int itemCount);
        void onCartClicked();
        void hide();


}
