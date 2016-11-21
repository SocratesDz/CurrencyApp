package com.socratesdiaz.currencyapp.views;

import android.app.Activity;
import android.content.Context;

/**
 * Created by socratesdiaz on 11/20/16.
 */
public interface MainView {
    public Activity getActivity();
    public void showToast(String message);
}
