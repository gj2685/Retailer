package com.example.retailer.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retailer.R;
import com.example.retailer.Utility;
import com.example.retailer.adapter.CartListAdapter;

public class CartActivity extends BaseActivity {
    CartListAdapter mAdapter;
    Cursor[] mCursors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCursors = mDBController.getCartInfo();

        ListView list = (ListView) findViewById(R.id.cart_list);
        mAdapter = new CartListAdapter(this, mCursors[0], true);
        list.setAdapter(mAdapter);

        setTotalCost();
    }

    private void setTotalCost() {
        Cursor cursor = mCursors[1];
        cursor.moveToFirst();
        double total = cursor.getDouble(cursor.getColumnIndex(Utility.TOTAL));
        total = ((int) (total * 100)) / 100.0;

        TextView txtViewTotalCost = (TextView) findViewById(R.id.cart_total_cost);
        txtViewTotalCost.setText(String.valueOf(total) + Utility.CURRENCY_INR);

        TextView txtViewTotalLable = (TextView) findViewById(R.id.cart_total_lable);
        String text = String.format(
                getResources().getString(R.string.cart_total),
                mCursors[0].getCount());
        txtViewTotalLable.setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public void removeProduct(int prodId) {
        boolean isDeleted = mDBController.removeFromCart(prodId);
        if (isDeleted) {
            Toast.makeText(getApplicationContext(), "Removed from cart", Toast.LENGTH_LONG).show();
            mCursors = mDBController.getCartInfo();
            mAdapter.changeCursor(mCursors[0]);
            mAdapter.notifyDataSetChanged();
            setTotalCost();
        } else {
            Toast.makeText(getApplicationContext(), "Error, couldn't remove from cart",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCursors != null) {
            if (mCursors[0] != null)
                mCursors[0].close();
            if (mCursors[1] != null)
                mCursors[1].close();
        }
    }
}