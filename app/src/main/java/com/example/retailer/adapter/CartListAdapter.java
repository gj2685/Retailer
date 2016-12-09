package com.example.retailer.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retailer.DbContentDescriptor;
import com.example.retailer.R;
import com.example.retailer.Utility;
import com.example.retailer.activity.CartActivity;
import com.example.retailer.activity.ProductDetailsActivity;

public class CartListAdapter extends CursorAdapter {
    CartActivity mActivity = null;

    public CartListAdapter(CartActivity activity, Cursor c, boolean autoRequery) {
        super(activity.getApplicationContext(), c, autoRequery);
        mActivity = activity;
    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        return View.inflate(context, R.layout.row_cart, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String prodName = null;
        double prodPrice = 0;
        int prodImgUrl = 0;
        int prodCount = 0;

        if (cursor != null) {
            final int prodId = cursor.getInt(cursor
                    .getColumnIndex(DbContentDescriptor.ProductTable.Cols.ID));
            prodName = cursor.getString(cursor
                    .getColumnIndex(DbContentDescriptor.ProductTable.Cols.PROD_NAME));
            prodPrice = cursor.getDouble(cursor
                    .getColumnIndex(DbContentDescriptor.ProductTable.Cols.PROD_PRICE));
            prodImgUrl = cursor.getInt(cursor
                    .getColumnIndex(DbContentDescriptor.ProductTable.Cols.PROD_IMG_URL));
            prodCount = cursor.getInt(cursor
                    .getColumnIndex(DbContentDescriptor.CartTable.Cols.PROD_COUNT));

            ImageButton btnRemove = (ImageButton) view.findViewById(R.id.product_remove);
            btnRemove.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.removeProduct(prodId);
                }
            });

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(mActivity, ProductDetailsActivity.class);
                    in.putExtra(Utility.PRODUCT_ID, prodId);
                    mActivity.startActivity(in);
                }
            });
        }

        TextView txtViewName = (TextView) view.findViewById(R.id.product_name);
        TextView txtViewPrice = (TextView) view.findViewById(R.id.product_price);
        ImageView imgView = (ImageView) view.findViewById(R.id.product_thumb);


        txtViewName.setText(prodName);
        txtViewPrice.setText(String.valueOf(prodPrice) + Utility.CURRENCY_INR);
        imgView.setImageResource(prodImgUrl);
    }
}