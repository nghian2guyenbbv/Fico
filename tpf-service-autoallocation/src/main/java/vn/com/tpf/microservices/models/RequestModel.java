package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
@Data
public class RequestModel<T> implements Serializable {

    private String product;
    private String clientId;
    private String scheme;
    private String applicationNo;
    private String sourceChannel;
    private Long loanAmountRequested;
    private String customerName;
    private String createdDate;
    private String stage;
    private String customerID;
    private String createdUser;
    private String leadId;
    private String status;

    public RequestModel() {
    }


}

