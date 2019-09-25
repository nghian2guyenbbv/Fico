package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ResponseModel<T> implements Serializable {

    private String request_id;
    private String reference_id;
    private Timestamp date_time;
    private int result_code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;

    public ResponseModel() {
        this.setResult_code(0);
    }

    public ResponseModel(T data) {
        this();
        this.data = data;
    }


    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public Timestamp getDate_time() {
        return date_time;
    }

    public void setDate_time(Timestamp date_time) {
        this.date_time = date_time;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(String format, String... params) {
        if (params != null && params.length > 0) {
            this.message = String.format(format, (Object[]) params);
        }
    }

    public void setFailMessage(String format, String... params) {
        this.setResult_code(500);
//        this.setDate_time(new Timestamp(new Date().getTime()));
        this.setData(null);
        if (params != null && params.length > 0) {
            this.message = String.format(format, (Object[]) params);
        } else {
            this.message = format;
        }
    }

}

