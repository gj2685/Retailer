package com.example.retailer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import com.example.retailer.DbContentDescriptor.CartTable;
import com.example.retailer.DbContentDescriptor.CategoryTable;
import com.example.retailer.DbContentDescriptor.ProductTable;
import com.example.retailer.model.Category;
import com.example.retailer.model.Product;

import java.util.ArrayList;

public class DbController {

    private static final DbController sController = new DbController();
    private static final String[] CATEGORY_PROJECTION = new String[]{
            CategoryTable.Cols.ID,
            CategoryTable.Cols.CAT_NAME,
            CategoryTable.Cols.CAT_ICON};
    private Activity mActivity;
    private DbOpenHelper mOpenHelper;

    private DbController() {

    }

    public static DbController getInstance() {
        return sController;
    }

    public void setDBController(Activity activity) {
        mActivity = activity;
        mOpenHelper = new DbOpenHelper(activity);
    }

    // inserting category into DB
    public void fillCategoryList() {
        String[] categories = mActivity.getResources().getStringArray(R.array.categories);
        int len = categories.length;
        ContentValues[] values = new ContentValues[len];

        for (int i = 0; i < len; i++) {
            values[i] = new ContentValues();
            values[i].put(CategoryTable.Cols.CAT_NAME, categories[i]);
        }

        mOpenHelper.bulkInsert(CategoryTable.CONTENT_URI, values);
        fillProductList(len);
    }

    // inserting products into DB
    public void fillProductList(int size) {
        Resources res = mActivity.getResources();
        for (int x = 0; x < size; x++) {
            String[][] products = {
                    res.getStringArray(R.array.products0),
                    res.getStringArray(R.array.products1),
                    res.getStringArray(R.array.products2),
                    res.getStringArray(R.array.products3)
            };

            TypedArray[] productsImg = {
                    res.obtainTypedArray(R.array.products0_img),
                    res.obtainTypedArray(R.array.products1_img),
                    res.obtainTypedArray(R.array.products2_img),
                    res.obtainTypedArray(R.array.products3_img)
            };

            int len = products.length;
            ContentValues[] values = new ContentValues[len];

            for (int i = 0; i < len; i++) {
                values[i] = new ContentValues();
                values[i].put(ProductTable.Cols.PROD_NAME, products[x][i]);
                double price = Math.random() * 400;
                price = ((int) (price * 100)) / 100.0; // rounding upto 2 decimals
                values[i].put(ProductTable.Cols.PROD_PRICE, price);
                values[i].put(ProductTable.Cols.CAT_ID, x + 1);
                values[i].put(ProductTable.Cols.PROD_IMG_URL,
                        productsImg[x].getResourceId(i, 0));
            }

            mOpenHelper.bulkInsert(ProductTable.CONTENT_URI, values);
            productsImg[x].recycle();
        }
    }

    public ArrayList<Category> getCategoryList() {
        ArrayList<Category> catList = new ArrayList<Category>();
        Cursor cursor = null;
        try {
            cursor = mOpenHelper.query(CategoryTable.CONTENT_URI,
                    CATEGORY_PROJECTION, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(
                            cursor.getColumnIndex(CategoryTable.Cols.ID));

                    String catName = cursor.getString(
                            cursor.getColumnIndex(CategoryTable.Cols.CAT_NAME));

                    Category info = new Category();
                    info.setName(catName);
                    info.setId(id);

                    catList.add(info);

                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return catList;
    }

    /**
     * @param catId
     * @return all products whose category id is <code>catId<code>
     */
    public ArrayList<Product> getProductList(int catId) {
        ArrayList<Product> prodList = new ArrayList<Product>();

        String where = ProductTable.Cols.CAT_ID + "=" + catId;
        Cursor cursor = null;
        try {
            cursor = mOpenHelper.query(
                    ProductTable.CONTENT_URI,
                    null, where, null, ProductTable.Cols.PROD_NAME + " ASC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    prodList.add(getProductInfo(cursor));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return prodList;
    }

    private Product getProductInfo(Cursor cursor) {
        int id = cursor.getInt(
                cursor.getColumnIndex(ProductTable.Cols.ID));
        int catId = cursor.getInt(
                cursor.getColumnIndex(ProductTable.Cols.CAT_ID));
        String prodName = cursor.getString(
                cursor.getColumnIndex(ProductTable.Cols.PROD_NAME));
        String prodDesc = cursor.getString(
                cursor.getColumnIndex(ProductTable.Cols.PROD_DESC));
        double prodPrice = cursor.getDouble(
                cursor.getColumnIndex(ProductTable.Cols.PROD_PRICE));
        int prodUrl = cursor.getInt(
                cursor.getColumnIndex(ProductTable.Cols.PROD_IMG_URL));

        Product info = new Product();
        info.setCategoryId(catId);
        info.setId(id);
        info.setName(prodName);
        info.setProdDesc(prodDesc);
        info.setPrice(prodPrice);
        info.setImageUrl(prodUrl);
        return info;
    }

    public Cursor getProductInfo(int Id) {
        String join = ProductTable.NAME + " left join " +
                CartTable.NAME + " on " +
                ProductTable.NAME + "." +
                ProductTable.Cols.ID + " = " +
                CartTable.NAME + "." +
                CartTable.Cols.PROD_ID;

        Uri uri = ProductTable.CONTENT_URI.buildUpon()
                .appendQueryParameter(Utility.JOIN, join).build();

        String where = ProductTable.NAME + "." +
                ProductTable.Cols.ID + "=" + Id;
        Cursor cursor = mOpenHelper.query(uri, null, where, null, null);
        return cursor;
    }

    public boolean addProductToCart(int id) {
        ContentValues values = new ContentValues();
        values.put(CartTable.Cols.PROD_ID, id);
        values.put(CartTable.Cols.PROD_COUNT, 1);

        Uri uri = mOpenHelper.insert(CartTable.CONTENT_URI, values);
        int rowId = Integer.parseInt(uri.getLastPathSegment());
        if (rowId != -1)
            return true;
        return false;
    }

    public Cursor[] getCartInfo() {
        Cursor[] cursors = new Cursor[2];
        String join = CartTable.NAME + " join " +
                ProductTable.NAME + " on " +
                ProductTable.NAME + "." +
                ProductTable.Cols.ID + " = " +
                CartTable.NAME + "." +
                CartTable.Cols.PROD_ID;
        String[] sumProjection = {
                "SUM(" + ProductTable.Cols.PROD_PRICE + ") as " + Utility.TOTAL};

        Uri uri = CartTable.CONTENT_URI.buildUpon()
                .appendQueryParameter(Utility.JOIN, join).build();

        cursors[0] = mOpenHelper.query(uri, null, null, null, null);
        cursors[1] = mOpenHelper.query(uri, sumProjection, null, null, null); // get total Cost
        return cursors;
    }

    public boolean removeFromCart(int prodId) {
        String where = CartTable.Cols.PROD_ID + "=" + prodId;
        int count = mOpenHelper.delete(CartTable.CONTENT_URI, where, null);
        if (count > 0)
            return true;
        return false;
    }
}