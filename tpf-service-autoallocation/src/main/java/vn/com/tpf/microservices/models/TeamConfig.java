package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ALLOCATION_USER_DETAIL")
@Data
public class TeamConfig {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TEAM_NAME")
    private String teamName;

    @Column(name = "TEAM_CODE")
    private String teamCode;

    @Column(name = "MAX_PENDING")
    private String maxPending;

    @Column(name = "MAX_QUOTA")
    private String maxQuota;

    @Column(name = "ASSIGN_FLAG")
    private String assignFlag;

    @Column(name = "USER_CREATE")
    private Timestamp userCreate;

    @Column(name = "CREATE_DATE")
    private Timestamp createDate;

    @Column(name = "USER_UPDATE")
    private String userUpdate;

    @Column(name = "UPDATE_DATE")
    private String updateDate;

    @Column(name = "COMPANY_NAME")
    private String companyName;


}
