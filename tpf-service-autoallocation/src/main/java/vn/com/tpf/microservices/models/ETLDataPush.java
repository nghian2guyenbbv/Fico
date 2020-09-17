package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ALLOCATION_ETL_DATAPUSH")
@Data
public class ETLDataPush {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "APP_NUMBER")
    private String appNumber;

    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "LEAD_ID")
    private Long leadId;

    @Column(name = "SOURCE_CHANNEL")
    private Timestamp suorceChanel;

    @Column(name = "CREATE_DATE")
    private String createDate;

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
    private String updateDate;

}
