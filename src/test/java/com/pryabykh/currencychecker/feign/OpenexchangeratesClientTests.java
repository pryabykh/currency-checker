package com.pryabykh.currencychecker.feign;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.pryabykh.currencychecker.dto.CurrencyResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;

@SpringBootTest
@ActiveProfiles("test")
public class OpenexchangeratesClientTests {
    private ResourceLoader resourceLoader;
    private WireMockServer wireMockServer;
    private OpenexchangeratesClient openexchangeratesClient;

    @Value("${openexchangerates.api.key}")
    private String apiKey;

    @Value("${mock.server_port}")
    private int mockServerPort;

    @BeforeEach
    public void setupMockServer() {
        wireMockServer = new WireMockServer(mockServerPort);
        wireMockServer.start();
    }

    @AfterEach
    public void tearDownServer() {
        wireMockServer.stop();
    }

    @Test
    public void fetchLatest() throws IOException {
        wireMockServer.stubFor(get("/latest.json?app_id=" + apiKey)
                .willReturn(ok()
                        .withBody(shapeCurrencyTodayJsonResponse())
                        .withHeader("Content-Type", "application/json")));

        CurrencyResponseDto currencyResponseDto = openexchangeratesClient.fetchLatest(apiKey);
        Assertions.assertNotNull(currencyResponseDto.getBase());
        Assertions.assertNotNull(currencyResponseDto.getRates());
        Assertions.assertNotNull(currencyResponseDto.getLicense());
        Assertions.assertNotNull(currencyResponseDto.getDisclaimer());
        Assertions.assertTrue(currencyResponseDto.getTimestamp() > 0);
        Assertions.assertEquals("USD", currencyResponseDto.getBase());
    }

    @Test
    public void fetchByDate() throws IOException {
        String yesterday = "2022-10-10";
        wireMockServer.stubFor(get("/historical/" + yesterday + ".json?app_id=" + apiKey)
                .willReturn(ok()
                        .withBody(shapeCurrencyYesterdayJsonResponse())
                        .withHeader("Content-Type", "application/json")));

        CurrencyResponseDto currencyResponseDto = openexchangeratesClient.fetchByDate(apiKey, yesterday);
        Assertions.assertNotNull(currencyResponseDto.getBase());
        Assertions.assertNotNull(currencyResponseDto.getRates());
        Assertions.assertNotNull(currencyResponseDto.getLicense());
        Assertions.assertNotNull(currencyResponseDto.getDisclaimer());
        Assertions.assertTrue(currencyResponseDto.getTimestamp() > 0);
        Assertions.assertEquals("USD", currencyResponseDto.getBase());
    }

    private String shapeCurrencyTodayJsonResponse() throws IOException {
        String jsonFilePath = "classpath:currencyRubGreaterResponse.json";
        return new String(resourceLoader.getResource(jsonFilePath).getInputStream().readAllBytes());
    }

    private String shapeCurrencyYesterdayJsonResponse() throws IOException {
        String jsonFilePath = "classpath:currencyRubLessResponse.json";
        return new String(resourceLoader.getResource(jsonFilePath).getInputStream().readAllBytes());
    }

    @Autowired
    public void setOpenexchangeratesClient(OpenexchangeratesClient openexchangeratesClient) {
        this.openexchangeratesClient = openexchangeratesClient;
    }

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
