package com.pryabykh.currencychecker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pryabykh.currencychecker.dto.CurrencyResponseDto;
import com.pryabykh.currencychecker.exceptions.CurrencyCodeIsNullException;
import com.pryabykh.currencychecker.exceptions.CurrencyCodeNotFoundException;
import com.pryabykh.currencychecker.exceptions.CurrencyRatesIsNullException;
import com.pryabykh.currencychecker.feign.OpenexchangeratesClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class OpenexchangeratesServiceTests {
    private OpenexchangeratesService currencyService;
    private ResourceLoader resourceLoader;
    private ObjectMapper objectMapper;
    @MockBean
    private OpenexchangeratesClient openexchangeratesClient;
    private final String currencyCode = "RUB";
    private final String nonExistentCurrencyCode = "TEST123";

    @Test
    public void currencyIsBiggerThanYesterday()
            throws CurrencyCodeIsNullException, CurrencyRatesIsNullException, CurrencyCodeNotFoundException, IOException {
        Mockito.when(openexchangeratesClient.fetchLatest(Mockito.anyString()))
                .thenReturn(shapeGreaterCurrencyResponse());
        Mockito.when(openexchangeratesClient.fetchByDate(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(shapeLessCurrencyResponse());
        boolean biggerThanYesterday = currencyService.isCurrencyBiggerThanYesterday(currencyCode);
        Assertions.assertTrue(biggerThanYesterday);
    }

    @Test
    public void currencyIsLessThanYesterday()
            throws CurrencyCodeIsNullException, CurrencyRatesIsNullException, CurrencyCodeNotFoundException, IOException {
        Mockito.when(openexchangeratesClient.fetchLatest(Mockito.anyString()))
                .thenReturn(shapeLessCurrencyResponse());
        Mockito.when(openexchangeratesClient.fetchByDate(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(shapeGreaterCurrencyResponse());
        boolean biggerThanYesterday = currencyService.isCurrencyBiggerThanYesterday(currencyCode);
        Assertions.assertFalse(biggerThanYesterday);
    }

    @Test
    public void throwsCurrencyCodeIsNullException() {
        assertThrows(CurrencyCodeIsNullException.class, () ->
                currencyService.isCurrencyBiggerThanYesterday(null));
    }

    @Test
    public void throwsCurrencyCodeNotFoundException() throws IOException {
        Mockito.when(openexchangeratesClient.fetchLatest(Mockito.anyString()))
                .thenReturn(shapeLessCurrencyResponse());
        Mockito.when(openexchangeratesClient.fetchByDate(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(shapeGreaterCurrencyResponse());
        assertThrows(CurrencyCodeNotFoundException.class, () ->
                currencyService.isCurrencyBiggerThanYesterday(nonExistentCurrencyCode));
    }

    @Test
    public void throwsCurrencyRatesIsNullException() {
        Mockito.when(openexchangeratesClient.fetchLatest(Mockito.anyString()))
                .thenReturn(new CurrencyResponseDto());
        Mockito.when(openexchangeratesClient.fetchByDate(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(new CurrencyResponseDto());
        assertThrows(CurrencyRatesIsNullException.class, () ->
                currencyService.isCurrencyBiggerThanYesterday(currencyCode));
    }

    private CurrencyResponseDto shapeGreaterCurrencyResponse() throws IOException {
        String jsonFilePath = "classpath:currencyRubGreaterResponse.json";
        return objectMapper.readValue(
                resourceLoader.getResource(jsonFilePath).getFile(), CurrencyResponseDto.class
        );
    }

    private CurrencyResponseDto shapeLessCurrencyResponse() throws IOException {
        String jsonFilePath = "classpath:currencyRubLessResponse.json";
        return objectMapper.readValue(
                resourceLoader.getResource(jsonFilePath).getFile(), CurrencyResponseDto.class
        );
    }

    @Autowired
    public void setCurrencyService(OpenexchangeratesService openexchangeratesService) {
        this.currencyService = openexchangeratesService;
    }

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
