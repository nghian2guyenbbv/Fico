package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ALLOCATION_REASSIGNED_DETAIL")
@Data
public class AllocationReassignedDetail {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "APP_NUMBER")
    private String appNumber;

    @Column(name = "CREATION_APPLSTAGE_TIME")
    private Timestamp creationApplStageTime;

    @Column(name = "CURRENT_CYCLE")
    private Long currentCycle;

    @Column(name = "STAGE_NAME")
    private String stageName;

    @Column(name = "STATUS_APP")
    private String statusApp;

    @Column(name = "CREATION_TIME_STAMP")
    private Timestamp creationTimeStamp;

    @Column(name = "ASSIGNEE")
    private String assignee;

    @Column(name = "TEAM_ASSIGNEE")
    private String teamAssignee;

    @Column(name = "APP_TYPE")
    private String appType;

    @Column(name = "SOURCE_CHANNEL")
    private String sourceChanel;

    @Column(name = "REASSIGN_BY")
    private String reassignBy;

    @Column(name = "REASSIGN_TO")
    private String reassignTo;

    @Column(name = "COMMENTS")
    private String comments;
}
