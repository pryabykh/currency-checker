package com.pryabykh.currencychecker.feign;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.pryabykh.currencychecker.dto.GifResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@ActiveProfiles("test")
public class GiphyApiClientTests {
    private ResourceLoader resourceLoader;
    private WireMockServer wireMockServer;
    private GiphyApiClient giphyApiClient;

    @Value("${giphy.api.key}")
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
    public void fetchRandomGif() throws IOException {
        String tag = "rich";
        wireMockServer.stubFor(get("/random?api_key=" + apiKey + "&tag=" + tag)
                .willReturn(ok()
                        .withBody(shapeRandomGifJsonResponse())
                        .withHeader("Content-Type", "application/json")));

        GifResponseDto gifResponseDto = giphyApiClient.fetchRandom(apiKey, tag);
        Assertions.assertNotNull(gifResponseDto.getData());
        Assertions.assertNotNull(gifResponseDto.getMeta());
    }

    private String shapeRandomGifJsonResponse() throws IOException {
        String jsonFilePath = "classpath:fetchRandomGifResponse.json";
        return new String(resourceLoader.getResource(jsonFilePath).getInputStream().readAllBytes());
    }

    @Autowired
    public void setGiphyApiClient(GiphyApiClient giphyApiClient) {
        this.giphyApiClient = giphyApiClient;
    }

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
