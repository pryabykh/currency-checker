package com.pryabykh.currencychecker.services;

import com.pryabykh.currencychecker.exceptions.*;

public interface CurrencyCheckerService {
    byte[] check(String currencyCode) throws CurrencyCodeIsNullException, CurrencyRatesIsNullException, CurrencyCodeNotFoundException, RandomGifIdIsNullException, GifDataIsNullException;
}
