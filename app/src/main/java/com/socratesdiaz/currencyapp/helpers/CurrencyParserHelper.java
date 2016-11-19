package com.socratesdiaz.currencyapp.helpers;

import com.socratesdiaz.currencyapp.models.Currency;
import com.socratesdiaz.currencyapp.utils.Constants;

import org.json.JSONObject;

/**
 * Created by socratesdiaz on 11/18/16.
 */
public class CurrencyParserHelper {

    public static Currency parseCurrency(JSONObject obj, String currencyName) {
        Currency currency = new Currency();
        currency.setBase(obj.optString(Constants.BASE));
        currency.setDate(obj.optString(Constants.DATE));

        JSONObject rateObject = obj.optJSONObject(Constants.RATES);
        if(rateObject != null) {
            currency.setRate(rateObject.optDouble(currencyName));
        }
        currency.setName(currencyName);

        return currency;
    }
}
