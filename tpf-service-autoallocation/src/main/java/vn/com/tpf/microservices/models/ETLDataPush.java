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
    private String leadId;

    @Column(name = "SOURCE_CHANNEL")
    private String suorceChanel;

    @Column(name = "CREATE_DATE")
    private Timestamp createDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CUST_NAME")
    private String cusName;

    @Column(name = "CUST_ID")
    private String cusId;

    @Column(name = "STAGE_NAME")
    private String stageName;

    @Column(name = "PRODUCT")
    private String product;

    @Column(name = "SCHEME")
    private String scheme;

    @Column(name = "LOAN_AMT")
    private Long loanAmt;

    @Column(name = "UPDATE_DATE")
    private String updateDate;

}
