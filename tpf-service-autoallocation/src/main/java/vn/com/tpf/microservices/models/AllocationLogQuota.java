package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ALLOCATION_LOG_QUOTA")
@Data
public class AllocationLogQuota {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "APP_NUMBER")
    private String appNumber;

    @Column(name = "CREATION_APPLSTAGE_TIME")
    private Timestamp creationApplStageTime;

    @Column(name = "CURRENT_CYCLE")
    private Long currentCycle;

    @Column(name = "STAGE_NAME")
    private String stageName;

    @Column(name = "CREATION_TIME_STAMP")
    private Timestamp creationTimeStamp;

    @Column(name = "ASSIGNEE")
    private String assignee;

    @Column(name = "TEAM_ASSIGNEE")
    private String teamAssignee;

    @Column(name = "ASSIGNED_BY")
    private String assignedBy;

    @Column(name = "APP_TYPE")
    private String appType;

    @Column(name = "SOURCE_CHANNEL")
    private String sourceChanel;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "METHOD_SUB_ADD")
    private String methodSubAdd;

    @Column(name = "QUOTA_PENDING")
    private String quotaPending;

    @Column(name = "OLD_VALUES")
    private int oldValues;

    @Column(name = "FREETEXT1")
    private String freetext1;

    @Column(name = "FREETEXT2")
    private String freetext2;
}
