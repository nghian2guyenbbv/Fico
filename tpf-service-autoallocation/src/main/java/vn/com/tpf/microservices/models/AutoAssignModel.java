package vn.com.tpf.microservices.models;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class AutoAssignModel<T> implements Serializable {

    private String appId;
    private String userName;
    private String result;
    private String messageResult;
    private Long id;

}

