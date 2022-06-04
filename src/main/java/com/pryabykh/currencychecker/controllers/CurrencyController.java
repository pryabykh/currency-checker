package com.pryabykh.currencychecker.controllers;

import com.pryabykh.currencychecker.exceptions.*;
import com.pryabykh.currencychecker.services.CurrencyCheckerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyController {
    private final CurrencyCheckerService currencyCheckerService;

    public CurrencyController(CurrencyCheckerService currencyCheckerService) {
        this.currencyCheckerService = currencyCheckerService;
    }

    @GetMapping("/currency/{code}")
    public ResponseEntity<?> checkCurrencyByCode(@PathVariable("code") String code) {
        try {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.IMAGE_GIF);
            return ResponseEntity.ok().headers(responseHeaders).body(currencyCheckerService.check(code));
        } catch (CurrencyCodeIsNullException | CurrencyCodeNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
