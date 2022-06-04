package com.pryabykh.currencychecker.dto;

import lombok.Data;

@Data
public class CurrencyPairDto {
    private double latest;
    private double yesterday;
}
