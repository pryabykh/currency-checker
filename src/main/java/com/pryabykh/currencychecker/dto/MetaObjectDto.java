package com.pryabykh.currencychecker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaObjectDto {
    @JsonProperty("msg")
    private String message;
    @JsonProperty("status")
    private int status;
    @JsonProperty("response_id")
    private String responseId;
}
