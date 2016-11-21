package com.socratesdiaz.currencyapp.presenters;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.socratesdiaz.currencyapp.models.Currency;
import com.socratesdiaz.currencyapp.receivers.CurrencyReceiver;
import com.socratesdiaz.currencyapp.services.CurrencyService;
import com.socratesdiaz.currencyapp.utils.Constants;
import com.socratesdiaz.currencyapp.utils.LogUtils;
import com.socratesdiaz.currencyapp.views.MainView;

/**
 * Created by socratesdiaz on 11/20/16.
 */
public class MainPresenter implements IMainPresenter, CurrencyReceiver.Receiver {

    private String mBaseCurrency = Constants.CURRENCY_CODES[30];
    private String mTargetCurrency = Constants.CURRENCY_CODES[0];

    private static final String TAG = MainPresenter.class.getSimpleName();

    private MainView mMainView;

    public MainPresenter(MainView mainView) {
        this.mMainView = mainView;
    }

    @Override
    public void onReceiveResult(int resultCode, final Bundle resultData) {
        switch (resultCode) {
            case Constants.STATUS_RUNNING:
                LogUtils.log(TAG, "CurrencyServiceRunning");
                break;
            case Constants.STATUS_FINISHED:
                mMainView.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Currency currencyParcel = resultData.getParcelable(Constants.RESULT);
                        if(currencyParcel != null) {
                            String message = "Currency " + currencyParcel.getBase() + " - " +
                                    currencyParcel.getName() + ": " + currencyParcel.getRate();
                            LogUtils.log(TAG, message);
                        }
                    }
                });
                break;
            case Constants.STATUS_ERROR:
                String error = resultData.getString(Intent.EXTRA_TEXT);
                LogUtils.log(TAG, error);
                mMainView.showToast(error);
        }
    }

    public void retrieveCurrencyExchangeRate() {
        CurrencyReceiver receiver = new CurrencyReceiver(new Handler());
        receiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, mMainView.getActivity().getApplicationContext(),
                CurrencyService.class);
        intent.setExtrasClassLoader(CurrencyService.class.getClassLoader());

        Bundle bundle = new Bundle();
        String url = Constants.CURRENCY_URL + mBaseCurrency;
        bundle.putString(Constants.URL, url);
        bundle.putParcelable(Constants.RECEIVER, receiver);
        bundle.putInt(Constants.REQUEST_ID, Constants.REQUEST_ID_NUM);
        bundle.putString(Constants.CURRENCY_NAME, mTargetCurrency);
        bundle.putString(Constants.CURRENCY_BASE, mBaseCurrency);
        intent.putExtra(Constants.BUNDLE, bundle);
        mMainView.getActivity().startService(intent);
    }
}
