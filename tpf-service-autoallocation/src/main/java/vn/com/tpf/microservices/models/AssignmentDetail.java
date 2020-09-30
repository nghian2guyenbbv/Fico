package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ALLOCATION_ASSIGNMENT_DETAIL")
@Data
public class AssignmentDetail {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "APP_NUMBER")
    private String appNumber;

    @Column(name = "CREATION_APPLSTAGE_TIME")
    private Timestamp creationApplStageTime;

    @Column(name = "STAGE_NAME")
    private String stageName;

    @Column(name = "STAGE_ID")
    private String stageId;

    @Column(name = "STATUS_APP")
    private String statusApp;

    @Column(name = "CREATION_TIME_STAMP")
    private Timestamp creationTimeStamp;

    @Column(name = "ASSIGNEE")
    private String assignee;

    @Column(name = "TEAM_ASSIGNEE")
    private String teamAssignee;

    @Column(name = "ASSIGNED_BY")
    private String assignedBy;

    @Column(name = "PICKUP_TIME")
    private Timestamp pickUptime;

    @Column(name = "ASSIGNED_TIME")
    private Timestamp assignedTime;

    @Column(name = "STATUS_ASSIGN")
    private String statusAssign;

    @Column(name = "COMPLETED_TIME")
    private Timestamp completedtime;

    @Column(name = "APP_TYPE")
    private String appType;

    @Column(name = "BOT_NAME")
    private String botName;

}
