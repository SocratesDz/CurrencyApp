package com.socratesdiaz.currencyapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.socratesdiaz.currencyapp.models.Currency;
import com.socratesdiaz.currencyapp.utils.Constants;
import com.socratesdiaz.currencyapp.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by socratesdiaz on 12/1/16.
 */

public class CurrencyTableHelper {
    private static final String TAG = CurrencyTableHelper.class.getSimpleName();
    private CurrencyDatabaseAdapter mAdapter;

    public CurrencyTableHelper(CurrencyDatabaseAdapter adapter) {
        mAdapter = adapter;
    }

    public long insertCurrency(Currency currency) {
        ArrayList<Currency> currencies = getCurrencyHistory(currency.getBase(), currency.getName(), currency.getDate());
        if(currencies.size() == 0) {
            LogUtils.log(TAG, "No records found in DB");

            ContentValues initialValues = new ContentValues();
            initialValues.put(Constants.KEY_BASE, currency.getBase());
            initialValues.put(Constants.KEY_DATE, currency.getDate());
            initialValues.put(Constants.KEY_RATE, currency.getRate());
            initialValues.put(Constants.KEY_NAME, currency.getName());

            long id = mAdapter.getWritableDatabase().insert(
                    Constants.CURRENCY_TABLE, null, initialValues);
            mAdapter.getWritableDatabase().close();
            return id;
        } else {
            LogUtils.log(TAG, "Found record on DB");
        }

        return currencies.get(0).getId();
    }

    public ArrayList<Currency> getCurrencyHistory(String base, String name, String date) {
        ArrayList<Currency> currencies = new ArrayList<>();
        Cursor cursor = mAdapter.getWritableDatabase().query(
                Constants.CURRENCY_TABLE,
                new String[] { Constants.KEY_ID, Constants.KEY_BASE, Constants.KEY_DATE,
                                Constants.KEY_RATE, Constants.KEY_NAME },
                Constants.KEY_BASE + " = ? AND " + Constants.KEY_NAME + " = ? AND " +
                Constants.KEY_DATE + " = ?",
                new String[] { base, name, date },
                null, null, null); // groupBy = having = orderBy = null

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                currencies.add(parseCurrency(cursor));
            }
            while(cursor.moveToNext()) {
                currencies.add(parseCurrency(cursor));
            }
        }

        return currencies;
    }

    public Currency getCurrency(long id) throws SQLException {
        Cursor cursor = mAdapter.getWritableDatabase().query(
                Constants.CURRENCY_TABLE,
                new String[] { Constants.KEY_ID, Constants.KEY_BASE, Constants.KEY_DATE,
                        Constants.KEY_RATE, Constants.KEY_NAME },
                Constants.KEY_ID + " = " + id, null, null, null, null);
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                return parseCurrency(cursor);
            }
        }
        return null;
    }

    private Currency parseCurrency(Cursor cursor) {
        Currency currency = new Currency();
        currency.setId(cursor.getLong(cursor.getColumnIndex(Constants.KEY_ID)));
        currency.setBase(cursor.getString(cursor.getColumnIndex(Constants.KEY_BASE)));
        currency.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_NAME)));
        currency.setRate(cursor.getDouble(cursor.getColumnIndex(Constants.KEY_RATE)));
        currency.setDate(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE)));

        return currency;
    }

    public void clearCurrencyTable() {
        mAdapter.getWritableDatabase().delete(Constants.CURRENCY_TABLE, null, null);
    }
}
