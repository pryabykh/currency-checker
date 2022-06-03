package com.pryabykh.currencychecker.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "giphy-download-client", url = "${giphy.download.url}")
public interface GiphyDownloadClient {
    @GetMapping("/{gifId}.gif")
    byte[] downloadGif(@PathVariable String gifId);
}
