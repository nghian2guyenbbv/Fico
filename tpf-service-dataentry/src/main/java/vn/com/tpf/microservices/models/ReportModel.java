package vn.com.tpf.microservices.models;

import lombok.Data;

import java.util.Date;

@Data
public class ReportModel {
    private String applicationId;
    private String status;
    private String fullName;
    private String identificationNumber;
    private Date createdDate;
    private Date updateDate;

    private String createdBy;
    private String saleBranch;
}
