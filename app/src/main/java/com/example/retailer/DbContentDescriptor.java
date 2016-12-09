package com.example.retailer;

import android.content.UriMatcher;
import android.net.Uri;

public class DbContentDescriptor {

    public static final String AUTHORITY = "com.example.retailer.MainContentProvider";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, ProductTable.PATH, ProductTable.PATH_TOKEN);
        uriMatcher.addURI(AUTHORITY, CategoryTable.PATH, CategoryTable.PATH_TOKEN);
        uriMatcher.addURI(AUTHORITY, CartTable.PATH, CartTable.PATH_TOKEN);
    }

    public static class CategoryTable {
        public static final String NAME = "category";
        public static final String PATH = NAME;
        public static final int PATH_TOKEN = 100;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(DbContentDescriptor.BASE_URI,
                NAME);

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
                Cols.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                Cols.CAT_NAME + " TEXT NOT NULL, " +
                Cols.CAT_ICON + " BLOB)";

        public static class Cols {
            public static final String ID = "_id";
            public static final String CAT_NAME = "cat_name";
            public static final String CAT_ICON = "cat_icon";
        }
    }

    public static class ProductTable {
        public static final String NAME = "products";
        public static final String PATH = NAME;
        public static final int PATH_TOKEN = 200;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH);

        public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
                Cols.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                Cols.CAT_ID + " INTEGER REFERENCES " +
                CategoryTable.NAME + "(" + CategoryTable.Cols.ID + "), " +
                Cols.PROD_NAME + " TEXT NOT NULL, " +
                Cols.PROD_DESC + " TEXT, " +
                Cols.PROD_PRICE + " DOUBLE NOT NULL, " +
                Cols.PROD_IMG_URL + " INTEGER)"; // resource id for saving locally else we can have http 'url' from server as String

        public static class Cols {
            public static final String ID = "_id";
            public static final String CAT_ID = "cat_id";
            public static final String PROD_NAME = "prod_name";
            public static final String PROD_DESC = "prod_desc";
            public static final String PROD_PRICE = "prod_price";
            public static final String PROD_IMG_URL = "prod_img_url";
        }
    }

    public static class CartTable {
        public static final String NAME = "cart";
        public static final String PATH = NAME;
        public static final int PATH_TOKEN = 300;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH);

        public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME + " (" +
                Cols.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                Cols.PROD_ID + " INTEGER REFERENCES " +
                ProductTable.NAME + "(" + ProductTable.Cols.ID + ") ON DELETE SET NULL, " +
                Cols.PROD_COUNT + " INTEGER DEFAULT 0)";

        public static class Cols {
            public static final String ID = "_id";
            public static final String PROD_ID = "prod_id";
            public static final String PROD_COUNT = "prod_count";
        }
    }
}