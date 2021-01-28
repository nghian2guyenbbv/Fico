package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "V_ALLOCATION_ASSIGN_CONFIG")
@Data
public class AssignConfigView {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "STAGE_NAME")
    private String stageName;

    @Column(name = "PRODUCT")
    private String product;

    @Column(name = "TEAM_NAME")
    private String teamName;

    @Column(name = "ASSIGN_FLAG")
    private String assignFlag;

    @Column(name = "USER_CREATE")
    private String userCreate;

    @Column(name = "CREATE_DATE")
    private Timestamp createDate;

    @Column(name = "USER_UPDATE")
    private String userUpdate;

    @Column(name = "UPDATE_DATE")
    private Timestamp updateDate;

    @Column(name = "SORT_INDEX")
    private Long sortIndex;

}
