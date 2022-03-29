package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;

@Data
public class RequestModel2 {

    @JsonProperty("requestID")
    @JsonAlias("request_id")
    private String request_id;

    @JsonProperty("paymentDate")
    @JsonAlias("date_time")
    private String date_time;

    @Valid
    private CancelTransModel data;
}

