package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ALLOCATION_USER_DETAIL")
@Data
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USER_NAME")
    private String userName;

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
    private int quotaApp;

    @Column(name = "PENDING_APP")
    private int pendingApp;
}
