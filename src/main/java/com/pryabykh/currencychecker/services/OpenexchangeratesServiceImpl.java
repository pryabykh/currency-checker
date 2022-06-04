package com.pryabykh.currencychecker.services;

import com.pryabykh.currencychecker.dto.CurrencyPairDto;
import com.pryabykh.currencychecker.dto.CurrencyResponseDto;
import com.pryabykh.currencychecker.exceptions.CurrencyCodeIsNullException;
import com.pryabykh.currencychecker.exceptions.CurrencyCodeNotFoundException;
import com.pryabykh.currencychecker.exceptions.CurrencyRatesIsNullException;
import com.pryabykh.currencychecker.feign.OpenexchangeratesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class OpenexchangeratesServiceImpl implements OpenexchangeratesService {
    private final OpenexchangeratesClient openexchangeratesClient;

    @Value("${openexchangerates.api.key}")
    private String apiKey;

    public OpenexchangeratesServiceImpl(OpenexchangeratesClient openexchangeratesClient) {
        this.openexchangeratesClient = openexchangeratesClient;
    }

    @Override
    public boolean isCurrencyBiggerThanYesterday(String currencyCode)
            throws CurrencyCodeIsNullException, CurrencyRatesIsNullException, CurrencyCodeNotFoundException {
        if (currencyCode == null) {
            throw new CurrencyCodeIsNullException();
        }
        CurrencyPairDto currencyPairDto = fetchCurrencyPair(currencyCode);
        return currencyPairDto.getLatest() > currencyPairDto.getYesterday();
    }

    private CurrencyPairDto fetchCurrencyPair(String currencyCode)
            throws CurrencyRatesIsNullException, CurrencyCodeNotFoundException {
        CurrencyPairDto currencyPairDto = new CurrencyPairDto();
        currencyPairDto.setLatest(fetchLatestCurrencyRate(currencyCode));
        currencyPairDto.setYesterday(fetchYesterdayCurrencyRate(currencyCode));
        return currencyPairDto;
    }

    private double fetchLatestCurrencyRate(String currencyCode)
            throws CurrencyRatesIsNullException, CurrencyCodeNotFoundException {
        CurrencyResponseDto currencyResponseDto = openexchangeratesClient.fetchLatest(apiKey);
        Map<String, Double> rates = currencyResponseDto.getRates();
        if (rates == null) {
            throw new CurrencyRatesIsNullException();
        }
        return findRateByCurrencyCode(rates, currencyCode);
    }

    private double fetchYesterdayCurrencyRate(String currencyCode)
            throws CurrencyRatesIsNullException, CurrencyCodeNotFoundException {
        String yesterday = fetchYesterdayDate();
        CurrencyResponseDto currencyResponseDto = openexchangeratesClient.fetchByDate(apiKey, yesterday);
        Map<String, Double> rates = currencyResponseDto.getRates();
        if (rates == null) {
            throw new CurrencyRatesIsNullException();
        }
        return findRateByCurrencyCode(rates, currencyCode);
    }

    private String fetchYesterdayDate() {
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));
        return today.minusDays(1).format(DateTimeFormatter.ISO_DATE);
    }

    private double findRateByCurrencyCode(Map<String, Double> rates, String currencyCode)
            throws CurrencyCodeNotFoundException {
        Double currencyRate = rates.get(currencyCode.toUpperCase());
        if (currencyRate == null) {
            throw new CurrencyCodeNotFoundException();
        }
        return currencyRate;
    }
}
