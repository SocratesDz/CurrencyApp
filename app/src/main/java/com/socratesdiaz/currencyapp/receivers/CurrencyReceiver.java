package com.socratesdiaz.currencyapp.receivers;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by socratesdiaz on 11/18/16.
 */
public class CurrencyReceiver extends ResultReceiver {

    private Receiver mReceiver;

    public CurrencyReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        this.mReceiver = receiver;
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
