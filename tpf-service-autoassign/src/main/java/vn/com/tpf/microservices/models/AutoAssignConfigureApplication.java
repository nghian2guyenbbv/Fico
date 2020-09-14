package vn.com.tpf.microservices.models;


import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "autoassign_configure_application", schema = "auto_assign")
public class AutoAssignConfigureApplication {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Generated(GenerationTime.INSERT)
    private long id_seq;

    private String vendorName;

    private long vendorId;

    private String requestId;

    private String referenceId;

    private Timestamp createdDate;

    private Timestamp updatedDate;

    public AutoAssignConfigureApplication() {
    }

}
