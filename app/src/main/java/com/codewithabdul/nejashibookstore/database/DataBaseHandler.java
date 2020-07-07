package com.codewithabdul.nejashibookstore.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHandler extends SQLiteOpenHelper {

    public DataBaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


//        db.execSQL(OrderTable.CREATE_ORDER_TBL);

        db.execSQL(OrderTable.CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // AppLog.localDbLog("DB Update Upgrading database from version " + oldVersion + " to " + newVersion);
    }
}
