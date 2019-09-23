package vn.com.tpf.microservices.models;

import java.sql.Timestamp;

public class RequestModel{
    private String request_id;
    private Timestamp date_time;
    private SearchModel data;

    public RequestModel() {
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public Timestamp getDate_time() {
        return date_time;
    }

    public void setDate_time(Timestamp date_time) {
        this.date_time = date_time;
    }

    public SearchModel getData() {
        return data;
    }

    public void setData(SearchModel data) {
        this.data = data;
    }
}

