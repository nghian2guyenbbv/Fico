package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ALLOCATION_USER_CHECKING")
@Data
public class UserChecking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "ACTIVE_FLAG")
    private String activeFlag;

    @Column(name = "USER_ROLE")
    private String userRole;

    @Column(name = "CREATE_DATE")
    private Timestamp createDate;

    @Column(name = "USER_LOGIN")
    private String userLogin;

    @Column(name = "CHECKED_FLAG")
    private String checkedFlag;

    @Transient
    private String teamName;

}
