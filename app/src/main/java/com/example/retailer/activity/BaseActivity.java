package com.example.retailer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.retailer.DbController;
import com.example.retailer.R;

public class BaseActivity extends AppCompatActivity {

    protected DbController mDBController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBController = DbController.getInstance();
        mDBController.setDBController(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_cart) {
            Intent in = new Intent(this, CartActivity.class);
            startActivity(in);
        }
        return false;
    }
}
