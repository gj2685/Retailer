package com.example.retailer;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "retailstore.db";
    private static final String LOGTAG = "DbOpenHelper";

    private static int DB_VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly())
            db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContentDescriptor.CategoryTable.CREATE_TABLE);
        db.execSQL(DbContentDescriptor.ProductTable.CREATE_TABLE);
        db.execSQL(DbContentDescriptor.CartTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int numValues = values.length;
        for (int i = 0; i < numValues; i++) {
            insert(uri, values[i]);
        }
        return numValues;
    }

    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            Log.i(LOGTAG, "inside insert method, db is null");
            return null;
        }

        int match = DbContentDescriptor.uriMatcher.match(uri);
        long rowID = -1;

        switch (match) {
            case DbContentDescriptor.CategoryTable.PATH_TOKEN:
                rowID = db.insert(DbContentDescriptor.CategoryTable.NAME, null, values);
                break;
            case DbContentDescriptor.ProductTable.PATH_TOKEN:
                rowID = db.insert(DbContentDescriptor.ProductTable.NAME, null, values);
                break;
            case DbContentDescriptor.CartTable.PATH_TOKEN:
                rowID = db.insert(DbContentDescriptor.CartTable.NAME, null, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown insert URI: " + uri);
        }

        if (rowID == -1) {
            Log.e(LOGTAG, "Error in inserting a row");
            return null;
        } else
            Log.i(LOGTAG, "inserting a record :: rowID: " + rowID);
        return ContentUris.withAppendedId(uri, rowID);
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            Log.i(LOGTAG, "inside query method, db is null");
            return null;
        }

        int match = DbContentDescriptor.uriMatcher.match(uri);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        /*
         * String groupBy = uri.getQueryParameter(Constant.GROUP_BY); String
         * limit = uri.getQueryParameter(Constant.LIMIT); boolean distinct =
         * uri.getQueryParameter(Constant.DISTINCT) != null;
         */
        String joinTables = uri.getQueryParameter(Utility.JOIN);

        switch (match) {
            case DbContentDescriptor.CategoryTable.PATH_TOKEN:
                qb.setTables(DbContentDescriptor.CategoryTable.NAME);
                break;
            case DbContentDescriptor.ProductTable.PATH_TOKEN:
                if (joinTables != null)
                    qb.setTables(joinTables);
                else
                    qb.setTables(DbContentDescriptor.ProductTable.NAME);
                break;
            case DbContentDescriptor.CartTable.PATH_TOKEN:
                if (joinTables != null)
                    qb.setTables(joinTables);
                else
                    qb.setTables(DbContentDescriptor.CartTable.NAME);
                break;
            default:
                throw new UnsupportedOperationException("Unknown query URI: " + uri);
        }
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        int rowsAffected = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        if (db == null) {
            Log.i(LOGTAG, "inside delete method, db is null");
            return rowsAffected;
        }

        int match = DbContentDescriptor.uriMatcher.match(uri);
        // String notIn = uri.getQueryParameter(Constant.NOT_IN);

        switch (match) {
            case DbContentDescriptor.CategoryTable.PATH_TOKEN:
                rowsAffected = db.
                        delete(DbContentDescriptor.CategoryTable.NAME, whereClause, whereArgs);
                break;
            case DbContentDescriptor.ProductTable.PATH_TOKEN:
                rowsAffected = db
                        .delete(DbContentDescriptor.ProductTable.NAME, whereClause, whereArgs);
                break;
            case DbContentDescriptor.CartTable.PATH_TOKEN:
                rowsAffected = db.
                        delete(DbContentDescriptor.CartTable.NAME, whereClause, whereArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown delete URI: " + uri);
        }
        return rowsAffected;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
