package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ALLOCATION_ETL_DATAPUSH", schema = "${spring.rabbitmq.schema}")
@Data
public class ETLDataPush {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "APP_NUMBER")
    private String appNumber;

    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "LEAD_ID")
    private Long leadId;

    @Column(name = "SOURCE_CHANNEL")
    private Timestamp suorceChanel;

    @Column(name = "CREATE_DATE")
    private Timestamp createDate;

    @Column(name = "STATUS")
    private Timestamp status;

    @Column(name = "CUST_NAME")
    private Timestamp cusName;

    @Column(name = "CUST_ID")
    private Timestamp cusId;

    @Column(name = "STAGE_NAME")
    private Timestamp stageName;

    @Column(name = "STAGE_ID")
    private Timestamp stageId;

    @Column(name = "PRODUCT")
    private Timestamp product;

    @Column(name = "SCHEME")
    private Timestamp scheme;

    @Column(name = "LOAN_AMT")
    private Timestamp loanAmt;

    @Column(name = "UPDATE_DATE")
    private Timestamp updateDate;

    @Column(name = "JSON_STRING")
    private Timestamp jsonString;

}
