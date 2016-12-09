
package com.example.retailer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retailer.model.Category;
import com.example.retailer.R;
import com.example.retailer.Utility;
import com.example.retailer.activity.ProductListActivity;

import java.util.ArrayList;

public class CategoryListAdapter extends BaseAdapter {

    private ArrayList<Category> mCategoryList;

    private Context mContext;

    private static int[] sCategoryIcons = {
            R.drawable.ic_electronics, R.drawable.ic_furnitures, R.drawable.ic_cat3,
            R.drawable.ic_cat4
    };

    class ViewHolder {
        ImageView imgView;
        TextView txtView;
    }

    public CategoryListAdapter(Context context, ArrayList<Category> categoryList) {
        mContext = context;
        mCategoryList = categoryList;
    }

    @Override
    public int getCount() {
        if (mCategoryList == null)
            return 0;
        return mCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int catID = mCategoryList.get(position).getId();
        String catName = mCategoryList.get(position).getName();
        
        if (convertView == null) {
            ViewHolder holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.row_category_list, null);

            holder.imgView = (ImageView)convertView.findViewById(R.id.category_thumb);
            holder.txtView = (TextView)convertView.findViewById(R.id.category_name);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder)convertView.getTag();

        holder.txtView.setText(catName);
        holder.imgView.setImageResource(sCategoryIcons[position]);

        CategoryClickListner clickListner = new CategoryClickListner(catID, catName);
        convertView.setOnClickListener(clickListner);

        return convertView;
    }

    class CategoryClickListner implements OnClickListener {
        private int catID;
        private String catName;

        public CategoryClickListner(int id, String name) {
            catID = id;
            catName = name;
        }

        @Override
        public void onClick(View v) {
            Intent in = new Intent(mContext, ProductListActivity.class);
            in.putExtra(Utility.CATEGORY_ID, catID);
            in.putExtra(Utility.CATEGORY_NAME, catName);

            mContext.startActivity(in);
        }
    }
}


