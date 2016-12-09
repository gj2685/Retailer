
package com.example.retailer.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ListView;

import com.example.retailer.adapter.ProductListAdapter;
import com.example.retailer.R;
import com.example.retailer.Utility;

public class ProductListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int catID = getIntent().getIntExtra(Utility.CATEGORY_ID, 1);
        String catName = getIntent().getStringExtra(Utility.CATEGORY_NAME);
        getSupportActionBar().setTitle(catName);

        init(catID);
    }

    private void init(int catID) {
        ListView prodList = (ListView)findViewById(R.id.product_list);

        ProductListAdapter adapter = new ProductListAdapter(this,
                mDBController.getProductList(catID));
        prodList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
