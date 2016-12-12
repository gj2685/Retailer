package com.example.retailer;


import android.util.Log;

public class Utility {
    public static final String DATABASE_LOADED = "loaded";
    public static final String CATEGORY_ID = "categoryID";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String PRODUCT_ID = "productID";
    public static final String JOIN = "join";
    public static final String TOTAL = "total";
    public static final String CURRENCY_INR = " INR";
    public static final String CURRENCY_USD = " $";
    private static final String LOG = "Retailer";

    public static void Logd(String msg) {
        Log.d(LOG, msg);
    }
}