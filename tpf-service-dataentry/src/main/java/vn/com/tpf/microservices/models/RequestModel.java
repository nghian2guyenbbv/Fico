package vn.com.tpf.microservices.models;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class RequestModel{
    private String request_id;
    private Timestamp date_time;
    private Application data;
    private List<Product> product;
    private List<ProductV2> productv2;
}

