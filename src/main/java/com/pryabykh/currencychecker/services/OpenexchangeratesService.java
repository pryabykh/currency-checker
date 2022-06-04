package com.pryabykh.currencychecker.services;

import com.pryabykh.currencychecker.exceptions.CurrencyCodeIsNullException;
import com.pryabykh.currencychecker.exceptions.CurrencyCodeNotFoundException;
import com.pryabykh.currencychecker.exceptions.CurrencyRatesIsNullException;

public interface OpenexchangeratesService {
    boolean isCurrencyBiggerThanYesterday(String currencyCode) throws CurrencyCodeIsNullException, CurrencyRatesIsNullException, CurrencyCodeNotFoundException;
}
