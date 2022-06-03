package com.pryabykh.currencychecker.feign;

import com.pryabykh.currencychecker.dto.GifResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "giphy-api-client", url = "${giphy.api.url}")
public interface GiphyApiClient {
    @GetMapping("/random")
    GifResponseDto fetchRandom(@RequestParam("api_key") String apiKey,
                               @RequestParam("tag") String tag);
}
