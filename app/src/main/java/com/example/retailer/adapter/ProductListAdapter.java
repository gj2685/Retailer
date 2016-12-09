
package com.example.retailer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retailer.activity.ProductDetailsActivity;
import com.example.retailer.model.Product;
import com.example.retailer.R;
import com.example.retailer.Utility;

import java.util.ArrayList;

public class ProductListAdapter extends BaseAdapter {

    private ArrayList<Product> mProductList;
    private Context mContext;

    class ViewHolder {
        ImageView imgView;
        TextView txtViewName;
        TextView txtViewPrice;
    }

    public ProductListAdapter(Context context, ArrayList<Product> productList) {
        mContext = context;
        mProductList = productList;
    }

    @Override
    public int getCount() {
        if (mProductList == null)
            return 0;
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String prodName = mProductList.get(position).getName();
        final int prodID = mProductList.get(position).getId();
        
        if (convertView == null) {
            ViewHolder holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.row_product_list, null);
            
            holder.txtViewName = (TextView)convertView.findViewById(R.id.product_name);
            holder.txtViewPrice = (TextView)convertView.findViewById(R.id.product_price);            
            holder.imgView = (ImageView)convertView.findViewById(R.id.product_thumb);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder)convertView.getTag();
        holder.txtViewName.setText(prodName);
        String price = String.valueOf(mProductList.get(position).getPrice()) + Utility.CURRENCY_INR;
        holder.txtViewPrice.setText(price);
        holder.imgView.setImageResource((mProductList.get(position).getImageUrl()));
        
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, ProductDetailsActivity.class);
                in.putExtra(Utility.PRODUCT_ID, prodID);
                mContext.startActivity(in);
            }
        });

        return convertView;
    }

}
