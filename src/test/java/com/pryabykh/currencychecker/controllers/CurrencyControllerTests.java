package com.pryabykh.currencychecker.controllers;

import com.pryabykh.currencychecker.exceptions.*;
import com.pryabykh.currencychecker.services.CurrencyCheckerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CurrencyController.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CurrencyControllerTests {
    private MockMvc mockMvc;
    private ResourceLoader resourceLoader;
    @MockBean
    private CurrencyCheckerService currencyCheckerService;
    private final String currencyCode = "RUB";

    @Test
    public void checkCurrencyByCode() throws Exception {
        Mockito.when(currencyCheckerService.check(Mockito.any())).thenReturn(shapeGifBytes());

        mockMvc.perform(get("/currency/" + currencyCode))
                .andExpect(status().isOk());
    }

    @Test
    public void catchCurrencyCodeIsNullException() throws Exception {
        Mockito.when(currencyCheckerService.check(Mockito.any())).thenThrow(CurrencyCodeIsNullException.class);

        mockMvc.perform(get("/currency/" + currencyCode))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void catchCurrencyCodeNotFoundException() throws Exception {
        Mockito.when(currencyCheckerService.check(Mockito.any())).thenThrow(CurrencyCodeNotFoundException.class);

        mockMvc.perform(get("/currency/" + currencyCode))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void catchCurrencyRatesIsNullException() throws Exception {
        Mockito.when(currencyCheckerService.check(Mockito.any())).thenThrow(CurrencyRatesIsNullException.class);

        mockMvc.perform(get("/currency/" + currencyCode))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void catchRandomGifIdIsNullException() throws Exception {
        Mockito.when(currencyCheckerService.check(Mockito.any())).thenThrow(RandomGifIdIsNullException.class);

        mockMvc.perform(get("/currency/" + currencyCode))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void catchGifDataIsNullException() throws Exception {
        Mockito.when(currencyCheckerService.check(Mockito.any())).thenThrow(GifDataIsNullException.class);

        mockMvc.perform(get("/currency/" + currencyCode))
                .andExpect(status().isInternalServerError());
    }


    private byte[] shapeGifBytes() throws IOException {
        String jsonFilePath = "classpath:testGif.gif";
        return resourceLoader.getResource(jsonFilePath).getInputStream().readAllBytes();
    }

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
