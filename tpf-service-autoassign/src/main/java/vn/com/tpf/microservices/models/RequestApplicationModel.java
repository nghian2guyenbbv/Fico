package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.List;

@Data
public class RequestApplicationModel {
    private String request_id;
    private String reference_id;
    private String date_time;
    private AutoAssignConfigureApplication data;

    public RequestApplicationModel() {}
}

