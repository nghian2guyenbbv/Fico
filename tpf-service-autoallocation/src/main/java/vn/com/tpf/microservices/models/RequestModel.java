package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

public class RequestModel<T> implements Serializable {
    private String request_id;
    private String date_time;

    public RequestModel() {
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

}

