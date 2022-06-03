package com.pryabykh.currencychecker.controllers;

import com.pryabykh.currencychecker.dto.GifResponseDto;
import com.pryabykh.currencychecker.feign.GiphyApiClient;
import com.pryabykh.currencychecker.feign.GiphyDownloadClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private GiphyApiClient giphyApiClient;
    private GiphyDownloadClient giphyDownloadClient;
    @Value("${giphy.api.key}")
    private String apiKey;

    @GetMapping
    public ResponseEntity<?> test() {
        GifResponseDto rich = giphyApiClient.fetchRandom(apiKey, "rich");
        byte[] fileSystemResource = giphyDownloadClient.downloadGif(rich.getData().getId());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.IMAGE_GIF);
//        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=" + rich.getData().getId() + ".gif");
        return ResponseEntity.ok().headers(responseHeaders).body(fileSystemResource);
    }

    @Autowired
    public void setGiphyApiClient(GiphyApiClient giphyApiClient) {
        this.giphyApiClient = giphyApiClient;
    }

    @Autowired
    public void setGiphyDownloadClient(GiphyDownloadClient giphyDownloadClient) {
        this.giphyDownloadClient = giphyDownloadClient;
    }
}
