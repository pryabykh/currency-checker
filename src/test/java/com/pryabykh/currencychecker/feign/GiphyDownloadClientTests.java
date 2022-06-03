package com.pryabykh.currencychecker.feign;

import com.github.tomakehurst.wiremock.WireMockServer;
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
public class GiphyDownloadClientTests {
    private ResourceLoader resourceLoader;
    private WireMockServer wireMockServer;
    private GiphyDownloadClient giphyDownloadClient;

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
        String gifId = "hJIxsnMB5DvlMtmCZk";
        wireMockServer.stubFor(get("/" + gifId + ".gif")
                .willReturn(ok()
                        .withBody(shapeGifBytes())
                        .withHeader("Content-Length", "2574302")
                        .withHeader("Content-Type", "image/gif")));

        byte[] bytesOfGif = giphyDownloadClient.downloadGif(gifId);
        Assertions.assertNotNull(bytesOfGif);
    }

    private byte[] shapeGifBytes() throws IOException {
        String jsonFilePath = "classpath:testGif.gif";
        return resourceLoader.getResource(jsonFilePath).getInputStream().readAllBytes();
    }

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Autowired
    public void setGiphyDownloadClient(GiphyDownloadClient giphyDownloadClient) {
        this.giphyDownloadClient = giphyDownloadClient;
    }
}
