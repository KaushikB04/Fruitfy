package com.example.fruitify_offcial;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Constant {

     public static String MerChanID = "PGTESTPAYUAT";

    public static String SALT_KEY = "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399";
    public static String apiEndPoint = "/pg/v1/pay";
    public static String merchantTransactionId = "txnId";

    public static String[] allProductCatName = {
                "Fruits","Vegetables", "DryFruits"
        };

        public static int[] allProductCatImg = {
                R.drawable.fruits,
                R.drawable.vegetable,
                R.drawable.dryfriuts
        };

}
