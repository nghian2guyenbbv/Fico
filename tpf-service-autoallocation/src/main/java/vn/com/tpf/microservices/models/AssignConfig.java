package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "ALLOCATION_USER_DETAIL")
@Data
public class AssignConfig {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String UserName;

    @Column(name = "TEAM_NAME")
    private String teamName;

    @Column(name = "TEAM_LEADER")
    private String teamLeader;

    @Column(name = "ACTIVE_FLAG")
    private String activeFlag;

    @Column(name = "USER_ROLE")
    private String userRole;

    @Column(name = "CREATED_DATE")
    private Timestamp createDate;

    @Column(name = "UPDATED_TIME")
    private Timestamp updateTime;

    @Column(name = "WORK_OFF")
    private String workOff;

    @Column(name = "QUOTA_APP")
    private String quotaApp;

    @Column(name = "PENDING_APP")
    private String pendingApp;

    @Column(name = "ASSIGN_FLAG")
    private String assignFlag;

}
