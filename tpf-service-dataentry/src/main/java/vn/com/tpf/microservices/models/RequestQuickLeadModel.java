package vn.com.tpf.microservices.models;

import lombok.Data;

@Data
public class RequestQuickLeadModel {
    private String request_id;
    private String date_time;
    private QuickLead data;

}

