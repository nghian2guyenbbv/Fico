package vn.com.tpf.microservices.models.eform;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "application_user", schema = "eform")
public class ApplicationUser {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "application_user_iq_seq"
    )
    @SequenceGenerator(name="application_user_iq_seq", sequenceName="eform.application_user_iq_seq",allocationSize=1)
    private int id;

    @Column(name = "applicationid")
    private String applicationId;

    private String username;

    private String description;

    private String note;

    @Column(name = "destatus")
    private String deStatus;

    private String stage;

    private int status;

    @Column(name = "createddate")
    private Timestamp createdDate;

    @Column(name = "updateddate")
    private Timestamp updatedDate;

    @Column(name = "createdby")
    private String createdBy;

    @Column(name = "updatedby")
    private String updatedBy;

    @Column(name = "lastassign")
    private Timestamp lastAssign;

    @Column(name = "laststart")
    private Timestamp lastStart;

    @Column(name = "starttime")
    private Timestamp startTime;

    @Column(name = "duration")
    private int duration;

    public String getDeStatus() {
        return deStatus;
    }
}
