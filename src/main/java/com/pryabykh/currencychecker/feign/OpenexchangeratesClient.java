package com.pryabykh.currencychecker.feign;

import com.pryabykh.currencychecker.dto.CurrencyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "openexchangerates-client", url = "${openexchangerates.api.url}")
public interface OpenexchangeratesClient {
    @GetMapping("/latest.json")
    CurrencyResponseDto fetchLatest(@RequestParam("app_id") String appId);

    @GetMapping("/historical/{date}.json")
    CurrencyResponseDto fetchByDate(@RequestParam("app_id") String appId,
                                           @PathVariable("date") String date);
}
