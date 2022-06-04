package com.pryabykh.currencychecker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pryabykh.currencychecker.dto.GifObjectDto;
import com.pryabykh.currencychecker.dto.GifResponseDto;
import com.pryabykh.currencychecker.exceptions.GifDataIsNullException;
import com.pryabykh.currencychecker.exceptions.RandomGifIdIsNullException;
import com.pryabykh.currencychecker.feign.GiphyApiClient;
import com.pryabykh.currencychecker.feign.GiphyDownloadClient;
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
public class GiphyServiceTests {
    private GiphyService giphyService;
    private ResourceLoader resourceLoader;
    private ObjectMapper objectMapper;
    @MockBean
    private GiphyApiClient giphyApiClient;
    @MockBean
    private GiphyDownloadClient giphyDownloadClient;

    @Test
    public void fetchRichGif() throws IOException, RandomGifIdIsNullException, GifDataIsNullException {
        Mockito.when(giphyApiClient.fetchRandom(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(shapeGifResponseDto());
        Mockito.when(giphyDownloadClient.downloadGif(Mockito.anyString()))
                .thenReturn(shapeGifBytes());

        byte[] richGif = giphyService.fetchRichGif();
        Assertions.assertNotNull(richGif);
    }

    @Test
    public void fetchBrokeGif() throws IOException, RandomGifIdIsNullException, GifDataIsNullException {
        Mockito.when(giphyApiClient.fetchRandom(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(shapeGifResponseDto());
        Mockito.when(giphyDownloadClient.downloadGif(Mockito.anyString()))
                .thenReturn(shapeGifBytes());

        byte[] brokeGif = giphyService.fetchRichGif();
        Assertions.assertNotNull(brokeGif);
    }

    @Test
    public void throwsRandomGifIdIsNullException() {
        Mockito.when(giphyApiClient.fetchRandom(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(shapeGifResponseDtoWithGifIdNull());

        assertThrows(RandomGifIdIsNullException.class, () ->
                giphyService.fetchRichGif());
    }

    @Test
    public void throwsGifDataIsNullException() {
        Mockito.when(giphyApiClient.fetchRandom(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(new GifResponseDto());

        assertThrows(GifDataIsNullException.class, () ->
                giphyService.fetchRichGif());
    }

    private GifResponseDto shapeGifResponseDto() throws IOException {
        String jsonFilePath = "classpath:fetchRandomGifResponse.json";
        return objectMapper.readValue(
                resourceLoader.getResource(jsonFilePath).getFile(), GifResponseDto.class
        );
    }

    private GifResponseDto shapeGifResponseDtoWithGifIdNull() {
        GifResponseDto gifResponseDto = new GifResponseDto();
        GifObjectDto gifObjectDto = new GifObjectDto();
        gifObjectDto.setId(null);
        gifResponseDto.setData(gifObjectDto);
        return gifResponseDto;
    }

    private byte[] shapeGifBytes() throws IOException {
        String jsonFilePath = "classpath:testGif.gif";
        return resourceLoader.getResource(jsonFilePath).getInputStream().readAllBytes();
    }

    @Autowired
    public void setGiphyService(GiphyService giphyService) {
        this.giphyService = giphyService;
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
