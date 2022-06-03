package com.pryabykh.currencychecker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GifResponseDto {
    @JsonProperty("data")
    private GifObjectDto data;
    @JsonProperty("meta")
    private MetaObjectDto meta;
}
