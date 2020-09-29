package vn.com.tpf.microservices.models;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Table(name = "V_ALLOCATION_HISTORY")
@Immutable
@Entity
@Data
public class AllocationHistoryView {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "APP_NUMBER")
    private String appNumber;
    @Column(name = "STAGE_NAME")
    private String stageName;
    @Column(name = "APP_TYPE")
    private String appType;
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
    @Column(name = "ASSIGNEE")
    private String assignee;
    @Column(name = "ASSIGNED_TIME")
    private Timestamp assignedTime;
    @Column(name = "STATUS_ASSIGN")
    private String statusAssign;
    @Column(name = "HOLE_REASON")
    private String holeReason;
    @Column(name = "TEAM_LEADER")
    private String teamLeader;

    @Column(name = "TEAM_NAME")
    private String teamName;
}
