package com.pryabykh.currencychecker.services;

import com.pryabykh.currencychecker.exceptions.*;
import org.springframework.stereotype.Service;

@Service
public class CurrencyCheckerServiceImpl implements CurrencyCheckerService {
    private final OpenexchangeratesService openexchangeratesService;
    private final GiphyService giphyService;

    public CurrencyCheckerServiceImpl(OpenexchangeratesService openexchangeratesService, GiphyService giphyService) {
        this.openexchangeratesService = openexchangeratesService;
        this.giphyService = giphyService;
    }

    @Override
    public byte[] check(String currencyCode)
            throws CurrencyCodeIsNullException, CurrencyRatesIsNullException, CurrencyCodeNotFoundException, RandomGifIdIsNullException, GifDataIsNullException {
        boolean currencyBiggerThanYesterday = openexchangeratesService.isCurrencyBiggerThanYesterday(currencyCode);
        if (currencyBiggerThanYesterday) {
            return giphyService.fetchRichGif();
        } else {
            return giphyService.fetchBrokeGif();
        }
    }
}
