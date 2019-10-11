package vn.com.tpf.microservices.models;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CancelModel {
    private String status;
    private String description;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
}