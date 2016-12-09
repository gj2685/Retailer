package com.example.retailer.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.retailer.adapter.CategoryListAdapter;
import com.example.retailer.R;
import com.example.retailer.Utility;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        populateDB();

        initCategories();
    }

    private void populateDB() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean loaded = prefs.getBoolean(Utility.DATABASE_LOADED, false);
        if (!loaded) {
            mDBController.fillCategoryList();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Utility.DATABASE_LOADED, true);
            editor.apply();
        }
    }

    private void initCategories() {
        GridView catList = (GridView) findViewById(R.id.category_list);

        CategoryListAdapter adapter = new CategoryListAdapter(this, mDBController.getCategoryList());
        catList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
