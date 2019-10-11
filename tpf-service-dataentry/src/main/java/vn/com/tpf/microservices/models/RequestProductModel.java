package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class RequestProductModel {
    private String request_id;
    private String date_time;
    private Search data;

}

