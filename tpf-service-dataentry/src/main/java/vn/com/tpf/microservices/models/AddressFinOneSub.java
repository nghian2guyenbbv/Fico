package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddressFinOneSub {

    @JsonProperty
    private String areaName;
    @JsonProperty
    private String areaCode;

}