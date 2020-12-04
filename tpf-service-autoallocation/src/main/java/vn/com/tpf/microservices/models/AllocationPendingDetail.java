package vn.com.tpf.microservices.models;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "ALLOCATION_PENDING_DETAIL")
@Immutable
@Entity
@Data
public class AllocationPendingDetail {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "APP_NUMBER")
    private String appNumber;
    @Column(name = "CREATION_APPLSTAGE_TIME")
    private Timestamp creationApplstageTime;
    @Column(name = "STAGE_NAME")
    private String stageName;
    @Column(name = "PENDING_CODE")
    private String pendingCode;
    @Column(name = "PENDING_COMMENTS")
    private String pendingComments;
    @Column(name = "PENDING_USER")
    private String pendingUser;
    @Column(name = "PENDING_DATE")
    private Timestamp pendingDate;
    @Column(name = "TEAM_USER")
    private String teamUser;
    @Column(name = "UPDATED_TIME")
    private Timestamp updatedTime;
    @Column(name = "USER_UPDATED")
    private String userUpdated;
    @Column(name = "CURRENT_CYCLE")
    private Long currentCycle;
}
