package vn.com.tpf.microservices.models;


import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "HISTORY_ALLOCATION", schema = "${spring.datasource.schema-username}")
@Data
public class HistoryAllocation {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name = "ID_HISTORY")
    private Long idHistory;

    @Column(name = "APPID")
    private String appId;

    @Column(name = "APPTYPE")
    private String appType;

    @Column(name = "APPSTATUS")
    private String appStatus;

    @Column(name = "ASSIGNED")
    private String assigned;

    @Column(name = "REASSIGNED")
    private String reAssigned;

    @Column(name = "CUSTOMERID")
    private String customerId;

    @Column(name = "CUSTOMERNAME")
    private String customerName;

    @Column(name = "LEADERID")
    private String leaderId;

    @Column(name = "STAGEID")
    private String stageId;

    @Column(name = "STAGENAME")
    private String stageName;

    @Column(name = "CREATEDAT")
    private Timestamp createAt;

    @Column(name = "ASSIGNEDAT")
    private Timestamp assignedAt;

    @Column(name = "UPDATEDAT")
    private Timestamp updatedAt;

}
