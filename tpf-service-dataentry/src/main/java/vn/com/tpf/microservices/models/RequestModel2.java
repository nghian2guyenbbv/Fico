package vn.com.tpf.microservices.models;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
@Data
public class RequestModel2<T> implements Serializable {
    private String request_id;
    private Timestamp date_time;
    private T data;
}
