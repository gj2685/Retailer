package com.example.retailer.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retailer.DbContentDescriptor;
import com.example.retailer.R;
import com.example.retailer.Utility;

public class ProductDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.Logd("ProductDetailsActivity : onCreate");

        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.Logd("ProductDetailsActivity : onResume");
        int prodID = getIntent().getIntExtra(Utility.PRODUCT_ID, 1);
        init(prodID);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Utility.Logd("ProductDetailsActivity : onNewIntent");
        setIntent(intent);
    }

    private void init(final int id) {
        Cursor cursor = mDBController.getProductInfo(id);
        String prodName = null;
        double prodPrice = 0;
        int prodImgUrl = 0;
        int prodCount = 0;

        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    prodName = cursor.getString(cursor
                            .getColumnIndex(DbContentDescriptor.ProductTable.Cols.PROD_NAME));
                    prodPrice = cursor.getDouble(cursor
                            .getColumnIndex(DbContentDescriptor.ProductTable.Cols.PROD_PRICE));
                    prodImgUrl = cursor.getInt(cursor
                            .getColumnIndex(DbContentDescriptor.ProductTable.Cols.PROD_IMG_URL));
                    prodCount = cursor.getInt(cursor
                            .getColumnIndex(DbContentDescriptor.CartTable.Cols.PROD_COUNT));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        TextView txtViewName = (TextView) findViewById(R.id.product_name);
        ImageView imgView = (ImageView) findViewById(R.id.product_image);
        TextView txtViewPrice = (TextView) findViewById(R.id.product_price);
        final AppCompatButton btnAddToCart = (AppCompatButton) findViewById(R.id.add_to_cart);

        txtViewName.setText(prodName);
        imgView.setImageResource(prodImgUrl);
        txtViewPrice.setText(String.valueOf(prodPrice) + Utility.CURRENCY_INR);
        if (prodCount > 0) {
            btnAddToCart.setEnabled(false);
            btnAddToCart.setSupportBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        } else {
            btnAddToCart.setEnabled(true);
            btnAddToCart.setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.statusBarDarkBlue)));
        }

        btnAddToCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean added = mDBController.addProductToCart(id);
                if (added) {
                    Toast.makeText(getApplicationContext(),
                            "Added to cart", Toast.LENGTH_LONG).show();
                    btnAddToCart.setEnabled(false);
                    btnAddToCart.setSupportBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    btnAddToCart.invalidate();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error, could not add to cart", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Utility.Logd("ProductDetailsActivity : onCreateOptionsMenu");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Utility.Logd("ProductDetailsActivity : onPrepareOptionsMenu");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utility.Logd("ProductDetailsActivity : onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Utility.Logd("ProductDetailsActivity : onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.Logd("ProductDetailsActivity : onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utility.Logd("ProductDetailsActivity : onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.Logd("ProductDetailsActivity : onDestroy");
    }
}