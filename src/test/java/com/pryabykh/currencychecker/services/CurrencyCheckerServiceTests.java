package com.pryabykh.currencychecker.services;

import com.pryabykh.currencychecker.exceptions.*;
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

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class CurrencyCheckerServiceTests {
    private CurrencyCheckerService currencyCheckerService;
    private ResourceLoader resourceLoader;
    private final String currencyCode = "RUB";

    @MockBean
    private OpenexchangeratesService openexchangeratesService;
    @MockBean
    private GiphyService giphyService;

    @Test
    public void rateIsGreaterThanYesterday() throws CurrencyCodeIsNullException, CurrencyRatesIsNullException, CurrencyCodeNotFoundException, RandomGifIdIsNullException, GifDataIsNullException, IOException {
        Mockito.when(openexchangeratesService.isCurrencyBiggerThanYesterday(Mockito.anyString()))
                .thenReturn(true);
        Mockito.when(giphyService.fetchRichGif())
                .thenReturn(shapeGifBytes());
        Mockito.when(giphyService.fetchBrokeGif())
                .thenReturn(shapeGifBytes());

        currencyCheckerService.check(currencyCode);
        Mockito.verify(giphyService).fetchRichGif();
    }

    @Test
    public void rateIsLessThanYesterday() throws CurrencyCodeIsNullException, CurrencyRatesIsNullException, CurrencyCodeNotFoundException, RandomGifIdIsNullException, GifDataIsNullException, IOException {
        Mockito.when(openexchangeratesService.isCurrencyBiggerThanYesterday(Mockito.anyString()))
                .thenReturn(false);
        Mockito.when(giphyService.fetchRichGif())
                .thenReturn(shapeGifBytes());
        Mockito.when(giphyService.fetchBrokeGif())
                .thenReturn(shapeGifBytes());

        currencyCheckerService.check(currencyCode);
        Mockito.verify(giphyService).fetchBrokeGif();
    }

    private byte[] shapeGifBytes() throws IOException {
        String jsonFilePath = "classpath:testGif.gif";
        return resourceLoader.getResource(jsonFilePath).getInputStream().readAllBytes();
    }

    @Autowired
    public void setCurrencyCheckerService(CurrencyCheckerService currencyCheckerService) {
        this.currencyCheckerService = currencyCheckerService;
    }

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
