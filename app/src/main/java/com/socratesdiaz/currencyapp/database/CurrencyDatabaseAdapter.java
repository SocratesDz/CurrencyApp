package com.socratesdiaz.currencyapp.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.socratesdiaz.currencyapp.utils.Constants;
import com.socratesdiaz.currencyapp.utils.LogUtils;

/**
 * Created by socratesdiaz on 12/1/16.
 */

public class CurrencyDatabaseAdapter extends SQLiteOpenHelper {

    private static final String TAG = CurrencyDatabaseAdapter.class.getName();

    public static final int DATABASE_VERSION = 1;

    public static final String CURRENCY_TABLE_CREATE =
            "create table " + Constants.CURRENCY_TABLE + " (" +
            Constants.KEY_ID + " integer primary key autoincrement, " +
            Constants.KEY_BASE + " text not null, " +
            Constants.KEY_NAME + " text not null, " +
            Constants.KEY_RATE + " real, " +
            Constants.KEY_DATE + " date);";

    public CurrencyDatabaseAdapter(Context context) {
        super(context, Constants.DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CURRENCY_TABLE_CREATE);
            LogUtils.log(TAG, "Currency table created");
        } catch (SQLException e) {
            e.printStackTrace();
            LogUtils.log(TAG, "Currency table creation error");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        clearCurrentTable(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    private void clearCurrentTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.CURRENCY_TABLE);
    }
}
